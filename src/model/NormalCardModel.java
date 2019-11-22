package model;

import constants.Enums;

import javax.swing.*;

public class NormalCardModel extends CardModel {

    public NormalCardModel(int cardNumber, Enums.Theme type, ImageIcon image, ImageIcon facingDownIcon) {
        super(cardNumber, type, image, facingDownIcon);
    }
}
