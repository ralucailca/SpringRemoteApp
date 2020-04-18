package client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Client;
import model.Excursie;
import services.AgentieException;
import services.IService;

import java.io.Serializable;


public class EditRezervariController implements Serializable {
    private IService server;
    private Excursie excursie;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNume;

    @FXML
    private TextField txtTel;

    @FXML
    private TextField txtBilete;

    public void setService(IService server, Excursie excursie){
        this.server = server;
        this.excursie=excursie;
    }

    @FXML
    void handleRezerva(ActionEvent event) {
        try{
            Client client=new Client(txtNume.getText(),txtTel.getText());
            //client.setId(Integer.parseInt(txtId.getText()));
            server.rezerva(excursie,client,Integer.parseInt(txtBilete.getText()));
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Rezervare",
                    "Rezervare efectuata pentru clientul: "+client);

            Stage stage= (Stage) txtId.getScene().getWindow();
            stage.close();
        }catch(AgentieException ex){
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }catch(NumberFormatException ex){
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }


}