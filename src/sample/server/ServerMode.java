package sample.server;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.FirstMenu;
import sample.Helper;
import sample.Main;
import sample.server.ServerGame;

import java.io.IOException;
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
                    while (true) {
                        try {
                            app.stop();
                            server.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                System.out.println(server.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                if (server.isClosed()) return;
                try {

                    Socket s1 = server.accept();
                    System.out.println("accepted");
                    Socket s2 = server.accept();
                    System.out.println("accepted2");
                    new ServerGame(s1, s2).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
