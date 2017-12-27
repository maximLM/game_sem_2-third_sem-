package sample.client;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import sample.Helper;
import sample.entities.Player;
import sample.entities.Question;
import sample.entities.CustomPair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientGameController {

    @FXML
    private TextField leftTop, leftBottom, rightTop, rightBottom;
    @FXML
    private ImageView rateImage;
    @FXML
    private TextArea questionArea;
    @FXML
    private ListView listView;

    private ClientGame model;
    private ClientMode clientMode;
    private GameView view;

    public ClientGameController() {

    }

    public void init(Player self,
                     Player enemy,
                     BufferedReader in,
                     PrintWriter out,
                     GameView view,
                     ClientMode mode) {
        model = new ClientGame(self, enemy, in, out, mode);
        clientMode = mode;
        this.view = view;
        leftTop.setText(self.getName());
        rightTop.setText(enemy.getName());
        leftBottom.setStyle("-fx-background-color: rgba(0, 0, 0, 0);"+
                "-fx-font-alignment: center;");
        rightBottom.setStyle("-fx-background-color: rgba(0, 0, 0, 0);"+
                "-fx-font-alignment: center;");
        view.setLeftTop(leftTop);
        view.setRightTop(rightTop);
        view.setLeftBottom(leftBottom);
        view.setRightBottom(rightBottom);
        view.setQuestionArea(questionArea);
        view.setListView(listView);
        rateImage.setVisible(false);
        view.setRateImage(rateImage);
        view.updateScore(self.getPoints(), enemy.getPoints());
        listView.setOnMouseClicked(event -> {
            int ind = listView.getSelectionModel().getSelectedIndex();
            if (ind == -1) return;
            onAnswerReceived(ind);
        });
        askQuestion();
    }


    private void askQuestion() {
        try {
            view.setWaitScene();
            Question question = model.getNewQuestion();
            view.showQuestion(question);
            view.updateScore(model.getSelf().getPoints(), model.getEnemy().getPoints());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onAnswerReceived(int nn) {
        final int n = nn + 1;

        model.sendAnswer(n);
        new Thread(() -> {
            try {
                CustomPair ok = model.getResult();
                Question q = model.getCurrentQuestion();
                view.showRightAnswer(q.getAnswers()[q.getRightAnswer() - 1],
                        q.getAnswers()[(int) (ok.getSecond() - 1)],
                        q.getAnswers()[n - 1],
                        ok.getFirst() == 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (model.getTimes() == 0) {
                Helper.onGameFinished((model.getSelf().getPoints() > model.getEnemy().getPoints()) ?
                        "You win" : "You lose",
                        clientMode.getApp(),
                        clientMode.getStage());
            } else {
                askQuestion();
            }
        }).start();
    }


}
