package model.game;

import constants.Enums;

public class HighScoreModel {

    private int id;
    private int score;
    private float elapsed_time;
    private Enums.GameMode game_mode;
    private String date;

    public HighScoreModel(int id, int score, float elapsed_time, Enums.GameMode game_mode, String date) {
        this.id = id;
        this.score = score;
        this.elapsed_time = elapsed_time;
        this.game_mode = game_mode;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getElapsed_time() {
        return elapsed_time;
    }

    public void setElapsed_time(float elapsed_time) {
        this.elapsed_time = elapsed_time;
    }

    public Enums.GameMode getGame_mode() {
        return game_mode;
    }

    public void setGame_mode(Enums.GameMode game_mode) {
        this.game_mode = game_mode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
