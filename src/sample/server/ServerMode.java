package sample.server;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.FirstMenu;
import sample.Helper;
import sample.server.ServerGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMode {
    private Stage stage;
    private Group root;
    private Scene scene;
    private ServerSocket server;
    public static int PORT = 1023;

    public ServerMode(Stage primaryStage) {
        stage = primaryStage;
        root = new Group();
        stage.setTitle("SERVER IS RUNNING");
        scene = new Scene(root, 400, 1);
        primaryStage.setScene(scene);

        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        server = new ServerSocket(PORT);
                        System.out.println(server.getInetAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                while (true) {
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
            }
        }).start();
    }

}
