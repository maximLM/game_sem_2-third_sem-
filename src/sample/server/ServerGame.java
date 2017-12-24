package sample.server;

import javafx.util.Pair;
import sample.Helper;
import sample.Question;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
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

    private void closeTwo(ServerPlayer one, ServerPlayer another) {
        System.out.println("CLOSING TWO");
        for (ServerPlayer a : new ServerPlayer[]{one, another}) {
            try {
                a.sendCloseMessage();
                a.getSocket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
    class ThreadForSend extends Thread{
        private ServerPlayer one, another;
        private CustomPair ans;
        private final int number;
        public ThreadForSend(ServerPlayer one, ServerPlayer another, CustomPair ans, int number) {
            this.one = one;
            this.number = number;
            this.another = another;
            this.ans = ans;
        }

        @Override
        public void run() {
            try {
                one.sendQuestion(number, ans);
            } catch (IOException|NumberFormatException e) {
                closeTwo(one, another);
            }
        }
    }

    @Override
    public void run() {
        try {
            one.getName();
            another.getName();
            one.sendName(another.getName());
            another.sendName(one.getName());
            for (int j = 0; j < Helper.TIMES; ++j) {
                System.out.println("j = " + j);
                CustomPair ans1 = new CustomPair(-1, -1L);
                CustomPair ans2 = new CustomPair(-1, -1L);
                int number = order.get(j);
                ThreadForSend t1 = new ThreadForSend(one, another, ans1, number);
                ThreadForSend t2 = new ThreadForSend(another, one, ans2, number);

                t1.start();
                t2.start();

                try {
                    t1.join();
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException|NumberFormatException e) {
            e.printStackTrace();
            closeTwo(one, another);
        }
    }
}
