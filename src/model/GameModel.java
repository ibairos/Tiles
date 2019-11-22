package model;

import constants.Enums;
import view.CardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static constants.Const.*;

public class GameModel {

    private PlayerModel[] playerModels;
    private int sizeX;
    private int sizeY;
    private GameParamsModel gameParams;
    private ArrayList<CardModel> cardModels;
    private Enums.Turn turn;

    public GameModel(GameParamsModel gameParams, ArrayList<CardModel> cardModels) {
        this.gameParams = gameParams;
        sizeX = gameParams.getColumns() * (SIZE_X_CARD + DEFAULT_SIZE_CARD_BORDER * 2);
        sizeY = gameParams.getRows() * (SIZE_Y_CARD + DEFAULT_SIZE_CARD_BORDER * 2) +
                DEFAULT_INITIAL_POS_Y_UPPER + DEFAULT_Y_SIZE_UPPER + 4 * DEFAULT_SIZE_BORDER;
        setCardModels(cardModels);
        setPlayerModels();
        turn = Enums.Turn.PLAYER_ONE;
    }

    public PlayerModel[] getPlayerModels() {
        return playerModels;
    }

    public void modifyScore(int delta) {
        int weighedDelta = delta * gameParams.getDifficulty();
        if (turn == Enums.Turn.PLAYER_ONE) {
            playerModels[0].setScore(playerModels[0].getScore() + weighedDelta);
        } else {
            playerModels[1].setScore(playerModels[1].getScore() + weighedDelta);
        }
    }

    public GameParamsModel getGameParams() {
        return gameParams;
    }

    public ArrayList<CardModel> getCardModels() {
        return cardModels;
    }

    private void setCardModels(ArrayList<CardModel> cardModels) {
        CardModel[] cards = new CardModel[cardModels.size() * 2];
        for (CardModel cardModel : cardModels) {
            Random r = new Random();
            for (int i = 0; i < 2; i++) {
                int index = -1;
                while (index < 0 || cards[index] != null) {
                    index = r.nextInt(cardModels.size() * 2);
                }
                try {
                    cards[index] = (CardModel) cardModel.clone();
                    cards[index].setIndex(index);
                } catch (CloneNotSupportedException e) {
                    System.out.println("Error when cloning card.");
                }
            }
        }
        this.cardModels = new ArrayList<>(Arrays.asList(cards));
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void changeTurn() {
        if (gameParams.getGameMode() != Enums.GameMode.ONE_PLAYER) {
            turn = turn == Enums.Turn.PLAYER_ONE ? Enums.Turn.PLAYER_TWO : Enums.Turn.PLAYER_ONE;
        }
    }

    public boolean isGameEnded() {
        for (CardModel card : cardModels) {
            if (card.getClass().equals(NormalCardModel.class) && !card.isCorrect()) {
                return false;
            }
        }
        return true;
    }

    private void setPlayerModels() {
        playerModels = new PlayerModel[2];
        switch (gameParams.getGameMode()) {
            case TWO_PLAYERS:
                playerModels[0] = new NormalPlayerModel("Player 1");
                playerModels[1] = new NormalPlayerModel("Player 2");
                break;
            case COMPUTER:
                playerModels[0] = new NormalPlayerModel("Player");
                playerModels[1] = new ComputerPlayerModel("Computer", gameParams);
                break;
            default:
                playerModels[0] = new NormalPlayerModel("Player");
                break;
        }
    }

    public Enums.Turn getTurn() {
        return turn;
    }

    public boolean isComputersTurn() {
        return turn == Enums.Turn.PLAYER_TWO
                && gameParams.getGameMode() == Enums.GameMode.COMPUTER;
    }

    public void updateComputerCardMap(ArrayList<CardView> cards) {
        if (gameParams.getGameMode() == Enums.GameMode.COMPUTER) {
            ((ComputerPlayerModel) playerModels[1]).updateCardMap(cards);
        }
    }
}

