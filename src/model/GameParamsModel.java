package model;

import constants.Enums;

import static constants.Const.DEFAULT_MAX_DIFFICULTY;

public class GameParamsModel {

    private int columns;
    private int rows;
    private int difficulty;
    private double cardsUpTime;
    private int numberOfNormalCards;
    private int numberOfBombCards;
    private Enums.GameMode gameMode;

    public GameParamsModel(int columns, int rows, int difficulty, double cardsUpTime, int numberOfCards,
                           Enums.GameMode gameMode) {
        this.columns = columns;
        this.rows = rows;
        this.difficulty = difficulty;
        this.cardsUpTime = cardsUpTime;
        this.gameMode = gameMode;
        numberOfBombCards = (int) ((double)numberOfCards * (((double)difficulty - 1) / DEFAULT_MAX_DIFFICULTY));
        numberOfNormalCards = numberOfCards - numberOfBombCards;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public double getCardsUpTime() {
        return cardsUpTime;
    }

    public int getNumberOfNormalCards() {
        return numberOfNormalCards;
    }

    public int getNumberOfBombCards() {
        return numberOfBombCards;
    }

    public Enums.GameMode getGameMode() {
        return gameMode;
    }

}
