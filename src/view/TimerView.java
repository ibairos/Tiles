package view;

import model.game.GameModel;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static constants.Const.*;

public class TimerView extends JLabel {

    private NumberFormat formatter;
    private GameModel gameModel;

    public TimerView(GameModel gameModel, Component parentComponent) {
        super();
        this.gameModel = gameModel;
        formatter = new DecimalFormat("#0.000");
        setSize(new Dimension(SIZE_X_TIMER, SIZE_Y_TIMER));
        setBounds(parentComponent.getWidth() / 2, parentComponent.getHeight() / 2, SIZE_X_TIMER,
                SIZE_Y_TIMER);
        setFont(new Font("Helvetica", Font.BOLD, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        double remainingTime = gameModel.getTurnRemainingTime();
        if (remainingTime > 0) {
            setText("Time: " + formatter.format(remainingTime));
        } else {
            setText("TIME OVER!");
        }
        if (remainingTime > GAME_TURN_TIMER / 2) {
            setForeground(Color.GREEN);
        } else if (remainingTime > GAME_TURN_TIMER / 3) {
            setForeground(Color.YELLOW);
        } else if (remainingTime > GAME_TURN_TIMER / 6) {
            setForeground(Color.ORANGE);
        } else {
            setForeground(Color.RED);
        }
        super.paintComponent(g);
    }

}
