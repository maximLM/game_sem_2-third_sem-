package sample;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.entities.Question;
import sample.entities.CustomPair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static final int TIMES = 9;
    private static final int ANSWERS = 4;
    public static final int WIDTH = 500, HEIGHT = 400;
    public static final String NEVER_STRING = "lkfjd;aklfjaoiewrajwrasdfadsfdas";

    private static List<Question> questions;

    public static final String CLOSING_MESSAGE = "SORRY_WE ARE _CLosing !324ksdf2";

    public static void onGameFinished(String message, Main app, Stage stage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("That's all");
            alert.setHeaderText("Great game, dude");
            alert.setContentText(message);
            alert.showAndWait();
            stage.close();
            System.exit(0);
            try {
                app.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static List<Question> parseQuestions() {
        if (questions == null) {
            questions = new ArrayList<>();
            try {
                BufferedReader in = new BufferedReader(
                        new FileReader(
                                new File("base.txt")
                        )
                );
                String s = null;
                while ((s = in.readLine()) != null) {
                    s = in.readLine();
                    String ans[] = new String[ANSWERS];
                    for (int i = 0; i < ANSWERS; ++i) {
                        ans[i] = in.readLine();
                    }
                    int right = Integer.parseInt(in.readLine());
                    in.readLine();
                    questions.add(new Question(s, right, ans));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return questions;
    }

    public static int getWinner(CustomPair ans1, CustomPair ans2, Question question) {
        int winner;
        if (ans1.getFirst() == question.getRightAnswer() &&
                ans2.getFirst() != question.getRightAnswer()) {
            winner = 1;
        } else if (ans1.getFirst() != question.getRightAnswer() &&
                ans2.getFirst() == question.getRightAnswer()) {
            winner = 2;
        } else if (ans1.getSecond() < ans2.getSecond()) {
            winner = 1;
        } else {
            winner = 2;
        }
        return winner;
    }
}
