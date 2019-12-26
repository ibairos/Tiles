package main;


import controller.MenuController;

import javax.swing.*;

public class TilesLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();
            } catch (Exception e) {
                System.exit(0);
            }
        });
    }

    public static void createAndShowGUI() {
        new MenuController();
    }

}
