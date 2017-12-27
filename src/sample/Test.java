package sample;

import org.junit.Before;
import sample.client.ClientGame;
import sample.client.ClientMode;
import sample.entities.Player;
import sample.entities.Question;
import sample.entities.CustomPair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Test {

    private Question question;

    @Before
    public void createQuestion() {
        question = mock(Question.class);
        when(question.getRightAnswer()).thenReturn(1);
    }

    @org.junit.Test
    public void testGetWinnerChoosesRightAnswer() {
        CustomPair ans1 = new CustomPair(1, 0);
        CustomPair ans2 = new CustomPair(2, 0);
        org.junit.Assert.assertEquals(1, Helper.getWinner(ans1, ans2, question));
    }

    @org.junit.Test
    public void testGetWinnerChoosesTheFasterAnswer() {
        CustomPair ans1 = new CustomPair(1, 9);
        CustomPair ans2 = new CustomPair(1, 10);
        org.junit.Assert.assertEquals(1, Helper.getWinner(ans1, ans2, question));
    }

    @org.junit.Test
    public void testGetIndexOfAnswer() {
        Question realQuestion = new Question("Question",
                1,
                "one",
                "two",
                "three",
                "four");
        org.junit.Assert.assertEquals(2, realQuestion.getAnwerIndex("three"));
    }

    @org.junit.Test
    public void testReadLineClosingAppWhenCloseMessageReceived() throws IOException {
        Player p = mock(Player.class);
        BufferedReader in = mock(BufferedReader.class);
        when(in.readLine()).thenReturn(Helper.CLOSING_MESSAGE);
        PrintWriter out = mock(PrintWriter.class);
        ClientMode mode = mock(ClientMode.class);
        ClientGame game = new ClientGame(p, p, in, out, mode);

        try {
            String s = game.readLine();
            System.out.println("s = " + s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
