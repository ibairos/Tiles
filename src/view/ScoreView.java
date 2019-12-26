package view;

import constants.Enums;
import model.game.GameModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static constants.Const.DEFAULT_SIZE_MIN_BORDER;

public class ScoreView extends JLabel {

    private GameModel gameModel;
    private int playerIndex;

    public ScoreView(GameModel gameModel, String playerName, int playerIndex) {
        super(playerName + ": " + 0);
        this.gameModel = gameModel;
        this.playerIndex = playerIndex;
        setBorder(new EmptyBorder(DEFAULT_SIZE_MIN_BORDER, DEFAULT_SIZE_MIN_BORDER, DEFAULT_SIZE_MIN_BORDER,
                DEFAULT_SIZE_MIN_BORDER));
    }

    @Override
    public void paintComponent(Graphics g) {
        setText(gameModel.getPlayerModels()[playerIndex].getPlayerName() + ": " + gameModel.getPlayerModels()[playerIndex].getScore());
        Color c =
                (playerIndex == 0 && gameModel.getTurn() == Enums.Turn.PLAYER_ONE) ||
                        (playerIndex == 1 && gameModel.getTurn() == Enums.Turn.PLAYER_TWO) ?
                        Color.RED :
                        Color.BLACK;
        setForeground(c);
        super.paintComponent(g);
    }

}
