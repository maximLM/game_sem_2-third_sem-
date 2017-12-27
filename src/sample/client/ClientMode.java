package sample.client;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import sample.Main;
import sample.entities.Player;
import sample.server.ServerMode;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Optional;

import static sample.Helper.HEIGHT;
import static sample.Helper.WIDTH;

public class ClientMode {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Stage stage;
    private Scene scene;
    private Group root;
    private Player self;
    private Main app;

    public ClientMode(Stage primaryStage, Main app) {
        this.app = app;
        stage = primaryStage;
        initStage();
        getName();
    }

    private void initStage() {
        root = new Group();
        scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    private void prepare() {
        try {
            socket = new Socket(InetAddress.getByName("localhost"), ServerMode.PORT);
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );
            stage.setOnCloseRequest((event) -> {
                try {
                    System.exit(0);
                    app.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            out = new PrintWriter(socket.getOutputStream());
            out.println(self.getName());
            out.flush();
            new Thread(() -> startGame()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startGame() {
        Player enemy = null;
        try {
            enemy = getEnemy();
            GameView view = new GameView(stage);
            synchronized (view) {
                try {
                    view.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ClientGameController controller = view.getController();
            controller.init(self, enemy, in, out, view, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onUserNamePicked(String name) {
        self = new Player(name);
        prepare();
    }

    public void getName() {
        for (; ; ) {
            TextInputDialog dialog = new TextInputDialog("Unknown");
            dialog.setTitle("Pick Username");
            dialog.setHeaderText("Welcome to my APPLICATION");
            dialog.setContentText("Please enter your name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                onUserNamePicked(result.get());
                break;
            }
        }
    }


    public Player getEnemy() throws IOException {
        String name = in.readLine();
        return new Player(name);
    }

    public Main getApp() {
        return app;
    }

    public Stage getStage() {
        return stage;
    }
}
