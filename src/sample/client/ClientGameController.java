package sample.client;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sample.Question;
import sample.server.CustomPair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientGameController {

    @FXML
    private TextField questionField, leftTop, leftBottom, rightTop, rightBottom, currentResult;
    @FXML
    private ListView listView;

    private ClientGame model;
    private ClientMode clientMode;
    private GameView view;


    public ClientGameController() {

    }

    public void init(Player self, Player enemy, BufferedReader in, PrintWriter out, GameView view, ClientMode mode) {
        model = new ClientGame(self, enemy, in, out);
        clientMode = mode;
        this.view = view;
        System.out.println("Created controller");
        System.out.println("self = " + self);
        System.out.println("enemy = " + enemy);
        leftTop.setText(self.getName());
        rightTop.setText(enemy.getName());
        view.setLeftTop(leftTop);
        view.setRightTop(rightTop);
        view.setLeftBottom(leftBottom);
        view.setRightBottom(rightBottom);
        view.setQuestionField(questionField);
        view.setListView(listView);
        currentResult.setVisible(false);
        view.setCurrentResult(currentResult);
        view.updateScore(self.getPoints(), enemy.getPoints());
        listView.setOnMouseClicked(event -> {
            System.out.println("clicked on " + listView.getSelectionModel().getSelectedIndex());
            onAnswerReceived(listView.getSelectionModel().getSelectedIndex());
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
                System.out.println("q.getRightAnswer() = " + q.getRightAnswer());
                view.showRightAnswer(q.getAnswers()[q.getRightAnswer() - 1],
                        q.getAnswers()[(int)(ok.getSecond() - 1)],
                        q.getAnswers()[n - 1],
                        ok.getFirst() == 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (model.getTimes() == 0) {
                view.setFinishScene(model.getSelf(), model.getEnemy());
                clientMode.onGameFinished();
            } else {
                askQuestion();
            }
        }).start();
    }


}
