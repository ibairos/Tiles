package util;

import constants.Enums;
import constants.Strings;
import model.BombCardModel;
import model.CardModel;
import model.NormalCardModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static constants.Const.*;
import static constants.Strings.DEFAULT_FILENAME_QUESTION_ICON;

public class FileIO {

    public static final FileNameExtensionFilter FILE_CHOOSER_FILTER =
            new FileNameExtensionFilter("Image files", "gif", "png", "bmp", "jpg", "jpeg");
    // Image scanners
    private static final String[] IMG_EXTENSIONS = new String[]{
            "gif", "png", "bmp", "jpg", "jpeg"
    };
    public static final FilenameFilter IMG_FILTER = (dir, name) -> {
        for (final String ext : IMG_EXTENSIONS) {
            if (name.toLowerCase().endsWith("." + ext)) {
                return true;
            }
        }
        return false;
    };

    public static ArrayList<CardModel> readNormalCards(File srcDir, int numberOfCards) {
        ArrayList<CardModel> cardModels = new ArrayList<>();
        if (srcDir.isDirectory()) {
            ImageIcon facingDownImage =
                    readCardIcon(new File(Strings.DEFAULT_FOLDER_RES + DEFAULT_FILENAME_QUESTION_ICON));
            for (final File f : Objects.requireNonNull(srcDir.listFiles(IMG_FILTER))) {
                String[] typeAndNumber = f.getName().split("-");
                Enums.Theme type = Enums.Theme.valueOf(typeAndNumber[0].toUpperCase());

                int number = Integer.parseInt(typeAndNumber[1].substring(0, typeAndNumber[1].lastIndexOf(".")));
                cardModels.add(new NormalCardModel(number, type,
                        readCardIcon(f),
                        facingDownImage));
            }
        }
        Collections.shuffle(cardModels, new Random(10));
        ArrayList<CardModel> shuffledCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            shuffledCards.add(cardModels.get(i));
        }
        return shuffledCards;
    }

    public static ArrayList<CardModel> readCustomCards(File srcDir, int numberOfCards) {
        ArrayList<CardModel> cardModels = new ArrayList<>();
        if (srcDir.isDirectory()) {
            ImageIcon facingDownImage =
                    readCardIcon(new File(Strings.DEFAULT_FOLDER_RES + DEFAULT_FILENAME_QUESTION_ICON));
            Enums.Theme type = Enums.Theme.CUSTOM;
            int cardNumber = 0;
            for (final File f : Objects.requireNonNull(srcDir.listFiles(IMG_FILTER))) {
                cardModels.add(new NormalCardModel(cardNumber, type,
                        readCardIcon(f),
                        facingDownImage));
                cardNumber++;
            }
        }
        Collections.shuffle(cardModels, new Random(10));
        ArrayList<CardModel> shuffledCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            shuffledCards.add(cardModels.get(i));
        }
        return shuffledCards;
    }

    public static ArrayList<CardModel> readBombCards(File srcFile, int numberOfBombCards) {
        ArrayList<CardModel> cardModels = new ArrayList<>();
        if (srcFile.isFile()) {
            ImageIcon facingDownImage =
                    readCardIcon(new File(Strings.DEFAULT_FOLDER_RES + DEFAULT_FILENAME_QUESTION_ICON));
            ImageIcon facingUpImage = readCardIcon(srcFile);
            for (int i = 0; i < numberOfBombCards; i++) {
                cardModels.add(new BombCardModel(facingUpImage, facingDownImage));
            }
        }
        return cardModels;
    }

    private static ImageIcon readCardIcon(File srcFile) {
        return readIcon(srcFile, SIZE_X_CARD, SIZE_Y_CARD);
    }

    public static ImageIcon readTurnIcon(File srcFile) {
        return readIcon(srcFile, SIZE_X_TURN, SIZE_Y_TURN);
    }

    public static ImageIcon readIcon(File srcFile, int sizeX, int sizeY) {
        ImageIcon image = null;
        if (srcFile.isFile()) {
            try {
                image = new ImageIcon((ImageIO.read(srcFile)).getScaledInstance(sizeX, sizeY,
                        Image.SCALE_SMOOTH));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public static File getDirectoryFromPath(String path) {
        return new File(path);
    }

}
