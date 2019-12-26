package model.card;


import constants.Enums;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class CardModel implements Cloneable {

    private int cardNumber;

    private int index;

    private Enums.Theme type;

    private Enums.CardState state;

    private ImageIcon normalImage;

    private ImageIcon fadedImage;

    private ImageIcon facingDownImage;

    public CardModel(int cardNumber, int index, Enums.CardState state) {
        this.cardNumber = cardNumber;
        this.index = index;
        this.state = state;
    }

    CardModel(int cardNumber, Enums.Theme type, ImageIcon normalImage, ImageIcon facingDownImage) {
        this.cardNumber = cardNumber;
        this.type = type;
        this.normalImage = normalImage;
        this.facingDownImage = facingDownImage;
        state = Enums.CardState.FACING_DOWN;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Enums.Theme getType() {
        return type;
    }

    public Enums.CardState getState() {
        return state;
    }

    public void setState(Enums.CardState state) {
        this.state = state;
    }

    public ImageIcon getImage() {
        ImageIcon img = null;
        switch (state) {
            case FACING_DOWN:
                img = facingDownImage;
                break;
            case FACING_UP:
                img = normalImage;
                break;
            case CORRECT:
                img = fadedImage;
                break;
        }
        return img;
    }

    public void createFadedImage() {
        BufferedImage bi = new BufferedImage(
                normalImage.getIconWidth(),
                normalImage.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        g.drawImage(normalImage.getImage(), 0, 0, null);
        fadedImage = new ImageIcon(bi);
    }

    public void setCorrect() {
        createFadedImage();
        state = Enums.CardState.CORRECT;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isCorrect() {
        return state.equals(Enums.CardState.CORRECT);
    }

    public boolean isFacingUp() {
        return state.equals(Enums.CardState.FACING_UP);
    }

    public boolean isFacingDown() {
        return state.equals(Enums.CardState.FACING_DOWN);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void flip() {
        switch (state) {
            case FACING_UP:
                state = Enums.CardState.FACING_DOWN;
                break;
            case FACING_DOWN:
                state = Enums.CardState.FACING_UP;
                break;
            default:
                break;
        }
    }

}
