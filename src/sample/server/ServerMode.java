package sample.server;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.FirstMenu;
import sample.Helper;
import sample.Main;
import sample.server.ServerGame;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerMode {
    private Stage stage;
    private Group root;
    private Scene scene;
    private ServerSocket server;
    public static int PORT = 1023;

    public ServerMode(Stage primaryStage, Main app) {
        stage = primaryStage;
        root = new Group();
        stage.setTitle("SERVER IS RUNNING");
        scene = new Scene(root, 400, 1);
        primaryStage.setScene(scene);

        new Thread(() -> {
            try {
                server = new ServerSocket(PORT);
                stage.setOnCloseRequest((event) -> {
                    try {
                        System.exit(0);
                        app.stop();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(server.getInetAddress());
            } catch (BindException e) {
                Helper.onGameFinished("Sorry server already exists", app, stage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                if (server.isClosed()) return;
                try {
                    Socket s1 = server.accept();
                    Socket s2 = server.accept();
                    new ServerGame(s1, s2).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
