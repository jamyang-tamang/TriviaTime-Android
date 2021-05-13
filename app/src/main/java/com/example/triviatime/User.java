package com.example.triviatime;

// Unused class, was going to be used for storing highscore in firebase
// and displaying them using a  recycler view in the end screen
public class User {
    private String name;
    private int score;

    public User(){

    }
    public User(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }


}
