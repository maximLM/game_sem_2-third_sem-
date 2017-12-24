package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static final int TIMES = 9;
    private static final int ANSWERS = 4;
    public static final int WIDTH = 500, HEIGHT = 400;
    public static final String NEVER_STRING = "jkf;ajraewr21945130834172ewufsjakdsllfjdsndvnkzfjou2491028";


    private static List<Question> questions;
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

            System.out.println(questions.get(3).getQuestion());
        }
        return questions;
    }
}
