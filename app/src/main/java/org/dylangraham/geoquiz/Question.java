package org.dylangraham.geoquiz;

public class Question {

    private int textResID;
    private boolean answerTrue;

    public Question(int textResID, boolean answerTrue) {
        this.textResID = textResID;
        this.answerTrue = answerTrue;
    }

    public int getTextResID() {
        return textResID;
    }

    public void setTextResID(int textResID) {
        this.textResID = textResID;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }
}
