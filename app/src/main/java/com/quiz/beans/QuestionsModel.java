package com.quiz.beans;

import java.io.Serializable;

/**
 * Created by Admin on 21/11/2018.
 */

public class QuestionsModel implements Serializable {
    String id = "";
    String questioncode = "";
    String question = "";
    String question_desc = "";
    String ch1 = "";
    String ch2 = "";
    String ch3 = "";
    String ch4 = "";
    String correctanswer = "";
    String questiontime = "";
    boolean isSkip = false;
    String chooseAnswer = "";

    public boolean isSkip() {
        return isSkip;
    }

    public void setSkip(boolean skip) {
        isSkip = skip;
    }

    public String getChooseAnswer() {
        return chooseAnswer;
    }

    public void setChooseAnswer(String chooseAnswer) {
        this.chooseAnswer = chooseAnswer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestioncode() {
        return questioncode;
    }

    public void setQuestioncode(String questioncode) {
        this.questioncode = questioncode;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion_desc() {
        return question_desc;
    }

    public void setQuestion_desc(String question_desc) {
        this.question_desc = question_desc;
    }

    public String getCh1() {
        return ch1;
    }

    public void setCh1(String ch1) {
        this.ch1 = ch1;
    }

    public String getCh2() {
        return ch2;
    }

    public void setCh2(String ch2) {
        this.ch2 = ch2;
    }

    public String getCh3() {
        return ch3;
    }

    public void setCh3(String ch3) {
        this.ch3 = ch3;
    }

    public String getCh4() {
        return ch4;
    }

    public void setCh4(String ch4) {
        this.ch4 = ch4;
    }

    public String getCorrectanswer() {
        return correctanswer;
    }

    public void setCorrectanswer(String correctanswer) {
        this.correctanswer = correctanswer;
    }

    public String getQuestiontime() {
        return questiontime;
    }

    public void setQuestiontime(String questiontime) {
        this.questiontime = questiontime;
    }
}
