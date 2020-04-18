package client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Agent;
import services.AgentieException;
import services.IService;

import java.io.IOException;

public class LoginController {
    private IService server;

    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField passField;

    public void setService(IService server) {
        this.server = server;
    }

    @FXML
    void handleLogin(ActionEvent event) {
        Agent a = new Agent(txtUser.getText(), passField.getText());
        try {
            FXMLLoader excursiiLoader = new FXMLLoader();
            excursiiLoader.setLocation(getClass().getResource("/excursiiView.fxml"));
            AnchorPane excursiiRoot = excursiiLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(excursiiRoot));
            stage.setTitle("Rezervare excursie: "+ a.getUser());
            stage.initModality(Modality.WINDOW_MODAL);
            System.out.println("merge1");
            ExcursiiController excursiiController = excursiiLoader.getController();
            excursiiController.setService(server, a);
            System.out.println("merge2");
            server.login(a, excursiiController);

            stage.show();
            excursiiController.initModel();

            txtUser.setText("");
            passField.setText("");
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (AgentieException ex){
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }

}