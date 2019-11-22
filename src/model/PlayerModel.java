package model;

public abstract class PlayerModel {

    private int score;
    private String playerName;

    PlayerModel(String playerName) {
        this.playerName = playerName;
        score = 0;
    }

    PlayerModel(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = Math.max(score, 0);
    }

    public String getPlayerName() {
        return playerName;
    }

}
