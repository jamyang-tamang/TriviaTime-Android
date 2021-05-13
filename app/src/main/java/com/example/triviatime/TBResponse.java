package com.example.triviatime;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.List;

public class TBResponse {
    private List<TBresponseValues> results;

    public List<TBresponseValues> getResults() {
        return results;
    }

    public class TBresponseValues {
        String question;
        @SerializedName("correct_answer")
        String correctAnswer;

        @SerializedName("incorrect_answers")
        List<String> incorrect_answers;


        public TBresponseValues(String question, String correctAnswer, List<String>incorrect_answers) {
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.incorrect_answers = incorrect_answers;
        }

        public String getQuestion() {
            return question;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public List<String> getIncorrectAnswers() {
            return incorrect_answers;
        }

    }
}
