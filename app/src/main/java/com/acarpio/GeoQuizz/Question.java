package com.acarpio.GeoQuizz;

public class Question {
    private String question;
    private int answerTrue;


    public Question(String question, int answerTrue) {
        this.question = question;
        this.answerTrue = answerTrue;

    }

    public int isAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(int answerTrue) {
        this.answerTrue = answerTrue;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
