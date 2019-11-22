package view;

import util.FileIO;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import static constants.Const.*;
import static constants.Strings.DEFAULT_FILENAME_BOMB_ICON;
import static constants.Strings.DEFAULT_FOLDER_RES;

public class BombIconView extends JPanel implements Runnable {

    private JLabel imageLabel;
    private JLayeredPane parent;
    private ArrayList<ImageIcon> images;

    public BombIconView(JLayeredPane parent) {
        super(null);
        this.parent = parent;
        setPreferredSize(new Dimension(FINAL_SIZE_BOMB, FINAL_SIZE_BOMB));
        int posX = DEFAULT_RELATIVE_POS_X_BOMB + FINAL_SIZE_BOMB < parent.getWidth() ? DEFAULT_RELATIVE_POS_X_BOMB : 0;
        int posY = DEFAULT_RELATIVE_POS_Y_BOMB + FINAL_SIZE_BOMB < parent.getWidth() ? DEFAULT_RELATIVE_POS_X_BOMB : 0;
        setBounds(posX, posY, FINAL_SIZE_BOMB, FINAL_SIZE_BOMB);
        setOpaque(false);
        imageLabel = new JLabel();
        add(imageLabel);
        images = new ArrayList<>();
        resetImage();
        loadBombs();

    }

    private void animate() {
        int increment = (FINAL_SIZE_BOMB - INITIAL_SIZE_BOMB) / BOMB_ANIMATION_STEPS;
        for (int i = 0; i <= BOMB_ANIMATION_STEPS; i++) {
            int size = INITIAL_SIZE_BOMB + i * increment;
            int relativePos = (FINAL_SIZE_BOMB - size) / 2;
            imageLabel.setBounds(relativePos, imageLabel.getBounds().y, imageLabel.getBounds().width,
                    imageLabel.getBounds().height);
            imageLabel.setIcon(images.get(i));
            SwingUtilities.invokeLater(this::repaint);
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        resetImage();
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    private void resetImage() {
        imageLabel.setBounds(0, 0, FINAL_SIZE_BOMB, FINAL_SIZE_BOMB);
        imageLabel.setIcon(FileIO.readIcon(new File(DEFAULT_FOLDER_RES + DEFAULT_FILENAME_BOMB_ICON), INITIAL_SIZE_BOMB, INITIAL_SIZE_BOMB));
    }

    private void loadBombs() {
        int increment = (FINAL_SIZE_BOMB - INITIAL_SIZE_BOMB) / BOMB_ANIMATION_STEPS;
        for (int i = 0; i <= BOMB_ANIMATION_STEPS; i++) {
            int size = INITIAL_SIZE_BOMB + i * increment;
            images.add(FileIO.readIcon(new File(DEFAULT_FOLDER_RES + DEFAULT_FILENAME_BOMB_ICON), size, size));
        }
    }

    @Override
    public void run() {
        parent.moveToFront(this);
        animate();
        parent.moveToBack(this);
    }
}
