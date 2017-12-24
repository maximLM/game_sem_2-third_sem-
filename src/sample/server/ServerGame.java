package sample.server;

import javafx.util.Pair;
import sample.Helper;
import sample.Question;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerGame extends Thread {
    private final ServerPlayer one, another;
    private List<Question> questions;
    private List<Integer> order;

    public ServerGame(Socket s1, Socket s2) {
        one = new ServerPlayer(s1);
        another = new ServerPlayer(s2);
        questions = Helper.parseQuestions();
        order = new ArrayList<>();
        for (int i = 0; i < questions.size(); ++i)
            order.add(i);
        Collections.shuffle(order);
    }

    @Override
    public void run() {
        try {
            one.getName();
            another.getName();
            one.sendName(another.getName());
            another.sendName(one.getName());
            for (int j = 0; j < Helper.TIMES; ++j) {
                CustomPair ans1 = new CustomPair(-1, -1L);
                int number = order.get(j);
                Thread t1 = new Thread(() -> {
                    try {
                        one.sendQuestion(number, ans1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                CustomPair ans2 = new CustomPair(-1, -1L);
                Thread t2 = new Thread(() -> {
                    try {
                        another.sendQuestion(number, ans2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                t1.start();
                t2.start();

                try {
                    System.out.println("waiting ans1");
                    t1.join();
                    System.out.println("received ans1");

                    System.out.println("waiting ans2");
                    t2.join();
                    System.out.println("received ans2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ans1 = " + ans1);
                System.out.println("ans2 = " + ans2);
                System.out.println("questions.get(i).getRightAnswer() = " + questions.get(number).getRightAnswer());

                int winner;
                if (ans1.getFirst() == questions.get(number).getRightAnswer() &&
                        ans2.getFirst() != questions.get(number).getRightAnswer()) {
                    winner = 1;
                } else if (ans1.getFirst() != questions.get(number).getRightAnswer() &&
                        ans2.getFirst() == questions.get(number).getRightAnswer()) {
                    winner = 2;
                } else if (ans1.getSecond() < ans2.getSecond()) {
                    winner = 1;
                } else {
                    winner = 2;
                }

                one.sendAnswer(winner == 1, ans2.getFirst());
                another.sendAnswer(winner == 2, ans1.getFirst());
                try {
                    Thread.sleep(17000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
