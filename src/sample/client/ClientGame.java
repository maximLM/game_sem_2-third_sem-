package sample.client;

import sample.Helper;
import sample.Question;
import sample.server.CustomPair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ClientGame {
    private Player self, enemy;
    private BufferedReader in;
    private PrintWriter out;
    private int times;
    private List<Question> questions;
    private Question currentQuestion;
    private int chosenAnswer;

    public ClientGame(Player self, Player enemy, BufferedReader in, PrintWriter out) {
        this.self = self;
        this.enemy = enemy;
        this.in = in;
        this.out = out;
        times = Helper.TIMES;
        questions = Helper.parseQuestions();
    }


    public Question getNewQuestion() throws IOException {
        System.out.println("asking");
        int n = Integer.parseInt(in.readLine());
        System.out.println("received n = " + n);
        return currentQuestion = questions.get(n);
    }

    public void sendAnswer(int n) {
        chosenAnswer = n;
        out.println(n);
        out.println(System.currentTimeMillis());
        out.flush();
    }

    public CustomPair getResult() throws IOException {
        int ok = Integer.parseInt(in.readLine());
        if (ok == 1) self.incPoints();
        else enemy.incPoints();
        --times;

        return new CustomPair(ok, Integer.parseInt(in.readLine()));
    }

    public Player getSelf() {
        return self;
    }

    public void setSelf(Player self) {
        this.self = self;
    }

    public Player getEnemy() {
        return enemy;
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public int getTimes() {
        return times;
    }
}
