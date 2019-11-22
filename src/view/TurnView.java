package view;

import constants.Enums;
import model.GameModel;

import javax.swing.*;
import java.awt.*;

import static constants.Const.*;

public class TurnView extends JLabel {

    private GameModel gameModel;
    private Component scoresPanel;

    public TurnView(ImageIcon image, GameModel gameModel, Component scoresPanel) {
        super(image);
        this.gameModel = gameModel;
        this.scoresPanel = scoresPanel;
        setSize(new Dimension(SIZE_X_TURN, SIZE_Y_TURN));
        setBorder(BorderFactory.createEmptyBorder(DEFAULT_SIZE_CARD_BORDER, DEFAULT_SIZE_CARD_BORDER,
                DEFAULT_SIZE_CARD_BORDER, DEFAULT_SIZE_CARD_BORDER));
    }

    @Override
    public void paintComponent(Graphics g) {
        // TURN
        Rectangle rectangle =
                gameModel.getTurn() == Enums.Turn.PLAYER_ONE ?
                        new Rectangle(scoresPanel.getX() + scoresPanel.getWidth(),
                                scoresPanel.getY() + scoresPanel.getHeight() / 3, getWidth(), getHeight()) :
                        new Rectangle(scoresPanel.getX() + scoresPanel.getWidth(),
                                scoresPanel.getY() + scoresPanel.getHeight() / 2, getWidth(), getHeight());
        setBounds(rectangle);
        super.paintComponent(g);
    }

}
