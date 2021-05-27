package com.example.seyedmohammadfinal;

import java.io.Serializable;

public class UserAnswer implements Serializable, Comparable<UserAnswer> {
    int firstNum, secondNum;
    double answer, userAnswer;
    boolean result;
    String difficulty, operation;


    public UserAnswer(int firstNum, int secondNum, double answer, double userAnswer, boolean difficulty, char operation) {
        this.firstNum = firstNum;
        this.secondNum = secondNum;
        this.answer = answer;
        this.userAnswer = userAnswer;
        if(answer == userAnswer) {
            this.result = true;
        }else this.result = false;

        if(difficulty){
            this.difficulty = "HARD";
        }else this.difficulty = "EASY";

        this.operation = String.valueOf(operation);

    }

    @Override
    public String toString() {
        return "DIFFICULTY: " + this.difficulty + "\n" +
                "Question: " + firstNum +
                " " + operation + " " + secondNum + "\n" +
                "Expected: " + answer +
                "  |  userAnswer: " + userAnswer + "\n" +
                "result: " + result + "\n";
    }


    @Override
    public int compareTo(UserAnswer o) {
        if(this.result && o.result || !this.result && !o.result) return 0;
        if(this.result) return 1;
        return -1;
    }
}
