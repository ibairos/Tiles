package model;


import constants.Enums;

import javax.swing.*;

public abstract class CardModel implements Cloneable {

    private int cardNumber;

    private int index;

    private Enums.Theme type;

    private Enums.CardState state;

    private ImageIcon image;

    private ImageIcon facingDownImage;

    public CardModel(int cardNumber, int index, Enums.CardState state) {
        this.cardNumber = cardNumber;
        this.index = index;
        this.state = state;
    }

    CardModel(int cardNumber, Enums.Theme type, ImageIcon image, ImageIcon facingDownImage) {
        this.cardNumber = cardNumber;
        this.type = type;
        this.image = image;
        this.facingDownImage = facingDownImage;
        state = Enums.CardState.FACING_DOWN;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public Enums.Theme getType() {
        return type;
    }

    public Enums.CardState getState() {
        return state;
    }

    public ImageIcon getImage() {
        return state == Enums.CardState.FACING_DOWN ? facingDownImage : image;
    }

    public void setCorrect() {
        state = Enums.CardState.CORRECT;
    }

    public int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setState(Enums.CardState state) {
        this.state = state;
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
