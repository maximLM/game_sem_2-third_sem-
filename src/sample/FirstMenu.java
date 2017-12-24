package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.client.ClientMode;
import sample.server.ServerMode;

import javax.swing.*;
import java.io.IOException;

public class FirstMenu {
    private Stage stage;
    private Parent root;
    private Scene scene;
    private Stage primaryStage;

    public FirstMenu(Stage primaryStage) {
        stage = primaryStage;
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/first_menu.fxml"));
        loader.setController(this);
        try {
            root = loader.load();
            stage.setTitle("Game");
            scene = new Scene(root, Helper.WIDTH, Helper.HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void gotoClientMode(ActionEvent event) {
        new ClientMode(primaryStage);
    }

    @FXML private void gotoServerMode(ActionEvent event) {
        new ServerMode(primaryStage);
    }
}
