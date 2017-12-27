package sample.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.Helper;
import sample.Main;
import sample.entities.Question;

import java.io.IOException;
import java.util.Arrays;

public class GameView {


    private final Stage stage;
    private Parent root;
    private Scene scene;
    private ClientGameController controller;

    private TextField  leftTop, leftBottom, rightTop, rightBottom;
    private TextArea questionArea;
    private ListView listView;
    private ImageView rateImage;

    public GameView(Stage stage) {
        this.stage = stage;
        Platform.runLater(
                () -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(sample.Main.class.getResource("/client.fxml"));
                        root = loader.load();
                        controller = loader.getController();
                        scene = new Scene(root, Helper.WIDTH, Helper.HEIGHT);
                        this.stage.setScene(scene);
                        this.stage.show();
                        synchronized (this) {
                            notify();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

    }

    public void setWaitScene() {
        // pass
    }

    public void showQuestion(Question question) {
        listView.setMouseTransparent(false);
        listView.setFocusTraversable(true);
        listView.setCellFactory((Callback<ListView<String>, ListCell<String>>) list -> {
            MyFormatCell formatCell = new MyFormatCell();
            formatCell.rightAnswer = Helper.NEVER_STRING;
            formatCell.oppenentAnswer = Helper.NEVER_STRING;
            formatCell.yourAnswer = Helper.NEVER_STRING;
            return formatCell;
        });

        Platform.runLater(() -> {
            rateImage.setVisible(false);
            listView.setVisible(true);
            questionArea.setVisible(true);
            questionArea.setText(question.getQuestion());
            ObservableList<String> list = FXCollections.observableList(
                    Arrays.asList(question.getAnswers())
            );
            listView.setItems(list);
        });
    }


    public void setRateImage(ImageView rateImage) {
        this.rateImage = rateImage;
    }

    class MyFormatCell extends ListCell<String> {
        String rightAnswer, oppenentAnswer, yourAnswer;

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(item);
            if (rightAnswer.equals(item)) setTextFill(Color.GREEN);
            else if (oppenentAnswer.equals(item)) setTextFill(Color.BLUE);
            else if (yourAnswer.equals(item)) setTextFill(Color.RED);
        }
    }

    public void showRightAnswer(String rightAnwer, String oppenentAnswer, String yourAnswer, boolean ok) {

        listView.setMouseTransparent(true);
        listView.setFocusTraversable(false);
        listView.setCellFactory((Callback<ListView<String>, ListCell<String>>) list -> {
            MyFormatCell formatCell = new MyFormatCell();
            formatCell.rightAnswer = rightAnwer;
            formatCell.oppenentAnswer = oppenentAnswer;
            formatCell.yourAnswer = yourAnswer;
            return formatCell;
        });
        rateImage.setVisible(true);
        Image img;

        if (ok) img = new Image(String.valueOf(Main.class.getResource("/good.png")));
        else img = new Image(String.valueOf(Main.class.getResource("/bad.png")));
        rateImage.setImage(img);
    }

    public ClientGameController getController() {
        return controller;
    }

    public void setLeftTop(TextField leftTop) {
        this.leftTop = leftTop;
    }

    public void setRightTop(TextField rightTop) {
        this.rightTop = rightTop;
    }

    public void setLeftBottom(TextField leftBotton) {
        this.leftBottom = leftBotton;
    }

    public void setRightBottom(TextField rightBottopm) {
        this.rightBottom = rightBottopm;
    }

    public void updateScore(int p1, int p2) {
        leftBottom.setText(p1 + "");
        rightBottom.setText(p2 + "");
    }

    public void setQuestionArea(TextArea questionArea) {
        this.questionArea = questionArea;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
