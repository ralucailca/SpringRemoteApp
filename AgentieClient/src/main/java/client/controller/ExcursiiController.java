package client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Agent;
import model.Excursie;
import services.IObserver;
import services.IService;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ExcursiiController extends UnicastRemoteObject implements IObserver, Serializable {
    private IService server;
    private Agent agent;
    private ObservableList<Excursie> excursii= FXCollections.observableArrayList();
    private ObservableList<Excursie> rezultatCautare= FXCollections.observableArrayList();

    @FXML
    private TableView<Excursie> tblExcursii;

    @FXML
    private TableColumn<Excursie, Integer> tblId;

    @FXML
    private TableColumn<Excursie, String> tblObiectiv;

    @FXML
    private TableColumn<Excursie, String> tblFirma;

    @FXML
    private TableColumn<Excursie, Integer> tblOra;

    @FXML
    private TableColumn<Excursie, Float> tblPret;

    @FXML
    private TableColumn<Excursie, Integer> tblLocuri;

    @FXML
    private TableView<Excursie> tblCauta;

    @FXML
    private TableColumn<Excursie, Integer> colId;

    @FXML
    private TableColumn<Excursie, String> colOb;

    @FXML
    private TableColumn<Excursie, String> colFirma;

    @FXML
    private TableColumn<Excursie, Integer> colOra;

    @FXML
    private TableColumn<Excursie, Float> colPret;

    @FXML
    private TableColumn<Excursie, Integer> colLocuri;

    @FXML
    private TextField txtora1;

    @FXML
    private TextField txtora2;

    @FXML
    private TextField txtObiectiv;

    @FXML
    private Label lblObiectiv;

    @FXML
    private Button btnLogout;

    public ExcursiiController() throws RemoteException {
    }

    public ExcursiiController(int port) throws RemoteException {
        super(port);
    }

    public ExcursiiController(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }


    @FXML
    public void initialize(){
        //tabel toate excursiile
        tblId.setCellValueFactory(new PropertyValueFactory<Excursie,Integer>("id"));
        tblObiectiv.setCellValueFactory(new PropertyValueFactory<Excursie, String>("obiectiv"));
        tblFirma.setCellValueFactory(new PropertyValueFactory<Excursie, String>("firma"));
        tblOra.setCellValueFactory(new PropertyValueFactory<Excursie, Integer>("oraPlecare"));
        tblPret.setCellValueFactory(new PropertyValueFactory<Excursie,Float>("pret"));
        tblLocuri.setCellValueFactory(new PropertyValueFactory<Excursie, Integer>("locuri"));
        tblExcursii.setRowFactory(t-> {return new TableRow<Excursie>(){

            public void updateItem(Excursie item, Boolean empty){
                super.updateItem(item, empty) ;
                if (item == null) {
                    setStyle("");
                } else if (item.getLocuri()== 8) {
                    for(int i=0; i<getChildren().size();i++){
                        ((Labeled) getChildren().get(i)).setTextFill(Color.RED);;
                    }
                    setTextFill(Color.RED);
                    setStyle("-fx-background-color: #ff5b5b;");
                } else {
                    setStyle("");
                }
            }};
        });
        tblExcursii.setItems(excursii);


        //tabel cautare
        colId.setCellValueFactory(new PropertyValueFactory<Excursie,Integer>("id"));
        colOb.setCellValueFactory(new PropertyValueFactory<Excursie, String>("obiectiv"));
        colFirma.setCellValueFactory(new PropertyValueFactory<Excursie, String>("firma"));
        colOra.setCellValueFactory(new PropertyValueFactory<Excursie, Integer>("oraPlecare"));
        colPret.setCellValueFactory(new PropertyValueFactory<Excursie,Float>("pret"));
        colLocuri.setCellValueFactory(new PropertyValueFactory<Excursie, Integer>("locuri"));
        tblCauta.setItems(rezultatCautare);

    }

    @FXML
    void handleCauta(ActionEvent event) {
        try {
            lblObiectiv.setText(txtObiectiv.getText());
            List<Excursie> rez = new ArrayList<>();
            server.cautaObiectivInterval(txtObiectiv.getText(), Integer.parseInt(txtora1.getText()), Integer.parseInt(txtora2.getText())).forEach(x -> rez.add(x));
            rezultatCautare.setAll(rez);
            if (rez.size() == 0)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Cautare", "Nu a fost gasita nici o excursie cu datele cerute!");

        }catch (NumberFormatException ex){
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

    @FXML
    void handleRezerva(ActionEvent event) {
        Excursie excursieSelected= tblExcursii.getSelectionModel().getSelectedItem();
        if(excursieSelected==null)
            excursieSelected=tblCauta.getSelectionModel().getSelectedItem();
        if(excursieSelected!=null){
            try {
                FXMLLoader editLoader = new FXMLLoader();
                editLoader.setLocation(getClass().getResource("/editRezervariView.fxml"));
                AnchorPane editroot = editLoader.load();
                Stage editStage = new Stage();
                editStage.setTitle("Rezervare");
                editStage.initModality(Modality.NONE);
                editStage.setScene(new Scene(editroot));

                EditRezervariController editStudentsController = editLoader.getController();
                editStudentsController.setService(server, excursieSelected);
                Stage primaryStage=(Stage) btnLogout.getScene().getWindow();
                editStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
            MessageAlert.showErrorMessage(null,"Nu a fost selectata nici o excursie pentru care sa se faca rezervarea!");
    }

    public void setService( IService server, Agent a) {
        this.server=server;
        this.agent=a;
        //initModel();
    }

    public void initModel(){
        List<Excursie> all=new ArrayList<>();
        server.findAll().forEach(x->all.add(x));
        excursii.setAll(all);
    }

    @FXML
    void handleLogout(ActionEvent event) {
        server.logout(agent, this);
        Stage stage= (Stage) btnLogout.getScene().getWindow();
        stage.close();
    }

    @Override
    public void update(List<Excursie> excursii) {
        this.excursii.setAll(excursii);
    }

}

