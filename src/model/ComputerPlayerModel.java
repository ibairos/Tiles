package model;

import view.CardView;

import java.util.ArrayList;

import static constants.Const.DEFAULT_MAX_DIFFICULTY;
import static constants.Const.UNKNOWN;

public class ComputerPlayerModel extends PlayerModel {

    private static final int[] UNKNOWN_PICK = {-1, -1};

    private GameParamsModel gameParams;
    private ArrayList<UnknownCardModel> cardMap;

    ComputerPlayerModel(String playerName, GameParamsModel gameParams) {
        super(playerName);
        this.gameParams = gameParams;
        cardMap = new ArrayList<>();
    }

    public int[] selectCards(ArrayList<CardView> cards) {
        int[] pickedIndexes = UNKNOWN_PICK;
        if (isRandomPick()) {
            // Random pick
            for (int i = 0; i < 2; i++) {
                pickedIndexes[i] = randomPick(cards);
            }
        } else {
            // Well thought pick
            updateCardMap(cards);
            int[] correctPick = correctPick();
            if (correctPick == UNKNOWN_PICK) {
                pickedIndexes[0] = randomPick(cards);
                int matchForPick = matchForPick(pickedIndexes[0]);
                if (matchForPick != UNKNOWN) {
                    pickedIndexes[1] = matchForPick;
                } else {
                    int randomPick = randomPick(cards);
                    while (randomPick == pickedIndexes[0]) {
                        randomPick = randomPick(cards);
                    }
                    pickedIndexes[1] = randomPick;
                }
            } else {
                pickedIndexes = correctPick;
            }
        }
        return pickedIndexes;
    }

    private boolean isRandomPick() {
        double randomPickProbability = 1 - ((double) gameParams.getDifficulty() - 1) / DEFAULT_MAX_DIFFICULTY;
        return Math.random() < randomPickProbability;
    }

    void updateCardMap(ArrayList<CardView> gameCards) {
        if (cardMap.size() != 0) {
            for (int i = 0; i < gameCards.size(); i++) {
                cardMap.get(i).setState(gameCards.get(i).getCardModel().getState());
                int cardNumber = cardMap.get(i).getCardNumber() == UNKNOWN
                        && !gameCards.get(i).getCardModel().isFacingDown() ?
                        gameCards.get(i).getCardModel().getCardNumber() : cardMap.get(i).getCardNumber();
                cardMap.get(i).setCardNumber(cardNumber);
            }
        } else {
            for (CardView view : gameCards) {
                int cardNumber = !view.getCardModel().isFacingDown()
                        ? view.getCardModel().getCardNumber() : UNKNOWN;
                cardMap.add(new UnknownCardModel(cardNumber, view.getCardModel().getIndex(),
                        view.getCardModel().getState()));
            }
        }
    }

    private int[] correctPick() {
        for (int i = 0; i < cardMap.size(); i++) {
            if (!cardMap.get(i).isFacingDown()) continue;
            for (int j = i + 1; j < cardMap.size(); j++) {
                if (!cardMap.get(j).isFacingDown()) continue;
                if (cardMap.get(i).getCardNumber() != UNKNOWN && cardMap.get(i).getCardNumber() == cardMap.get(j).getCardNumber()) {
                    return new int[]{i, j};
                }
            }
        }
        return UNKNOWN_PICK;
    }

    private int randomPick(ArrayList<CardView> cards) {
        int pick = UNKNOWN;
        while (pick == UNKNOWN
                || !cards.get(pick).getCardModel().isFacingDown()) {
            pick = (int) (Math.random() * cards.size());
        }
        return pick;
    }

    private int matchForPick(int pick) {
        for (int i = 0; i < cardMap.size(); i++) {
            if (i != pick && i == cardMap.get(i).getCardNumber() && cardMap.get(i).isFacingDown()) {
                return i;
            }
        }
        return UNKNOWN;
    }

}
