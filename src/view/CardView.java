package view;

import model.CardModel;

import javax.swing.*;
import java.awt.*;

import static constants.Const.*;

public class CardView extends JLabel {

    private CardModel cardModel;
    private int index;

    public CardView(CardModel cardModel, int index) {
        super();
        this.cardModel = cardModel;
        this.index = index;
        setSize(new Dimension(SIZE_X_CARD + 2 * DEFAULT_SIZE_CARD_BORDER,
                SIZE_Y_CARD + 2 * DEFAULT_SIZE_CARD_BORDER));
        setBorder(BorderFactory.createEmptyBorder(DEFAULT_SIZE_CARD_BORDER, DEFAULT_SIZE_CARD_BORDER,
                DEFAULT_SIZE_CARD_BORDER, DEFAULT_SIZE_CARD_BORDER));
    }

    @Override
    public void paintComponent(Graphics g) {
        setIcon(cardModel.getImage());
        super.paintComponent(g);
    }

    public CardModel getCardModel() {
        return cardModel;
    }

    public int getIndex() {
        return index;
    }

}
