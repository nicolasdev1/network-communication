package Client.Login;

import Client.Model.Data;
import Client.Room.RoomViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {
    @FXML
    public TextField server_ip;

    @FXML
    public TextField port;

    @FXML
    public TextField name;

    public String server_port;

    public void onClick() throws IOException {
        Data.ip = server_ip.getText();
        this.server_port = port.getText();
        Data.name = name.getText();
        Data.port = Integer.parseInt(server_port);

        Stage stage;
        stage = (Stage) server_ip.getScene().getWindow();
        Parent root = FXMLLoader.load(LoginViewController.class.getResource("../Room/RoomView.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle(Data.name);
        stage.setOnCloseRequest(event -> {
            RoomViewController.thread.interrupt();
            System.exit(0);
        });
        stage.setResizable(false);
        stage.show();
    }
}
