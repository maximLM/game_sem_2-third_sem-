package sample;

public class Question {
    private String question;
    private String[] answers;
    private int rightAnswer;

    public Question(String question, int rightAnswer, String ... ans) {
        this.question = question;
        this.answers = ans;
        this.rightAnswer = rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
