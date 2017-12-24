package sample.client;

import sample.Helper;
import sample.Question;
import sample.server.CustomPair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.List;

public class ClientGame {
    private Player self, enemy;
    private BufferedReader buf;
    private PrintWriter out;
    private int times;
    private List<Question> questions;
    private Question currentQuestion;
    private ClientMode clientMode;

    public ClientGame(Player self, Player enemy, BufferedReader in, PrintWriter out, ClientMode mode) {
        this.clientMode = mode;
        this.self = self;
        this.enemy = enemy;
        this.buf = in;
        this.out = out;
        times = Helper.TIMES;
        questions = Helper.parseQuestions();
    }


    public Question getNewQuestion() throws IOException {
        System.out.println("asking");
        int n = Integer.parseInt(readLine());
        System.out.println("received n = " + n);
        return currentQuestion = questions.get(n);
    }

    public void sendAnswer(int n) {
        out.println(n);
        out.println(System.currentTimeMillis());
        out.flush();
    }

    public CustomPair getResult() throws IOException {
        int ok = Integer.parseInt(readLine());
        if (ok == 1) self.incPoints();
        else enemy.incPoints();
        --times;

        return new CustomPair(ok, Integer.parseInt(readLine()));
    }

    private String readLine() throws IOException {
        try {
            String ret = buf.readLine();
            if (ret.equals(Helper.CLOSING_MESSAGE))
                clientMode.onGameFinished("Sorry your oppenent disconnected");
            return ret;
        } catch (IOException e) {
            clientMode.onGameFinished("Sorry your oppenent disconnected");
        }
        return null;
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

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public int getTimes() {
        return times;
    }
}
