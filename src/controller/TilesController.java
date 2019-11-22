package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static constants.Const.*;

public abstract class TilesController extends JFrame implements ActionListener, WindowListener {

    private int sizeX;
    private int sizeY;

    TilesController(String title, int sizeX, int sizeY) throws HeadlessException {
        super(title);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        setSize(sizeX, sizeY);
        setLocation(DEFAULT_X_POS, DEFAULT_Y_POS);
        setResizable(FRAME_RESIZEABLE);
    }

    public abstract void init();

    public abstract void destroy();

    public Dimension getDimensions() {
        return new Dimension(sizeX, sizeY);
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
