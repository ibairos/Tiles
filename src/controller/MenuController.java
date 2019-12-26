package controller;

import constants.Enums;
import model.card.CardModel;
import model.game.GameModel;
import model.game.GameParamsModel;
import model.game.HighScoreModel;
import util.FileIO;
import util.SQLite;
import view.HighScoresView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static constants.Commands.*;
import static constants.Const.*;
import static constants.Strings.*;
import static util.FileIO.FILE_CHOOSER_FILTER;
import static util.FileIO.IMG_FILTER;

public class MenuController extends TilesController {

    private static final String FRAME_TITLE = "Tiles - Menu";

    private JPanel jPanel;

    private JSpinner sizeXSpinner;
    private JSpinner sizeYSpinner;
    private JSlider difficultySlider;
    private JSlider cardsUpTimeSlider;
    private JButton loadThemeButton;
    private JComboBox<String> themesComboBox;
    private ButtonGroup opponentRadioGroup;

    private SQLite sqLite;
    private JFileChooser fc;

    private ArrayList<CardModel> customCards;

    public MenuController() throws HeadlessException {
        super(FRAME_TITLE, DEFAULT_X_SIZE, DEFAULT_Y_SIZE);
        init();
    }

    @Override
    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupJPanel();
        add(jPanel);
        setVisible(true);
        sqLite = new SQLite();
        fc = new JFileChooser();
        fc.setFileFilter(FILE_CHOOSER_FILTER);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setCurrentDirectory(new File(DEFAULT_FOLDER_RES).getAbsoluteFile());
    }

    @Override
    public void destroy() {
        // It does nothing in this implementation
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case MENU_SHOW_ABOUT_PAGE:
                showAboutPage();
                break;
            case MENU_LOAD_THEME:
                loadTheme();
                break;
            case MENU_REMOVE_THEME:
                removeTheme();
                break;
            case MENU_SHOW_HIGH_SCORES:
                showHighScores();
                break;
            case MENU_START:
                startGame();
                break;
            case MENU_EXIT:
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                break;
            default:
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        destroy();
    }

    private void setupJPanel() {

        // Initialize JPanel
        jPanel = new JPanel(null);

        // Upper part
        JButton aboutButton = new JButton("Tiles Rules");
        aboutButton.setBounds(DEFAULT_INITIAL_POS_X_ABOUT, DEFAULT_INITIAL_POS_Y_ABOUT, DEFAULT_X_SIZE_ABOUT_BUTTON,
                DEFAULT_Y_SIZE_ABOUT_BUTTON);
        aboutButton.setMnemonic(KeyEvent.VK_R);
        aboutButton.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        aboutButton.setActionCommand(MENU_SHOW_ABOUT_PAGE);
        aboutButton.addActionListener(this);
        jPanel.add(aboutButton);

        // Difficulty
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        difficultyPanel.setBounds(DEFAULT_RELATIVE_POS_X_DIFFICULTY,
                aboutButton.getY() + aboutButton.getHeight() + DEFAULT_RELATIVE_POS_Y_DIFFICULTY,
                DEFAULT_X_SIZE, DEFAULT_Y_SIZE_DIFFICULTY);
        difficultyPanel.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        difficultySlider = new JSlider(JSlider.HORIZONTAL, DEFAULT_MIN_DIFFICULTY, DEFAULT_MAX_DIFFICULTY,
                DEFAULT_DIFFICULTY);
        difficultySlider.setMajorTickSpacing(1);
        difficultySlider.setPaintTicks(true);
        difficultySlider.setPaintLabels(true);
        JLabel difficultyLabel = new JLabel("Difficulty: ", JLabel.CENTER);
        difficultyPanel.add(difficultyLabel);
        difficultyPanel.add(difficultySlider);
        jPanel.add(difficultyPanel);

        // Up time
        JPanel cardsUpTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cardsUpTimePanel.setBounds(DEFAULT_RELATIVE_POS_X_CARDS_UP_TIME,
                difficultyPanel.getY() + difficultyPanel.getHeight() + DEFAULT_RELATIVE_POS_Y_CARDS_UP_TIME,
                DEFAULT_X_SIZE, DEFAULT_Y_SIZE_CARDS_UP_TIME);
        cardsUpTimePanel.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        cardsUpTimeSlider = new JSlider(JSlider.HORIZONTAL, DEFAULT_MIN_CARDS_UP_TIME, DEFAULT_MAX_CARDS_UP_TIME,
                DEFAULT_CARDS_UP_TIME);
        cardsUpTimeSlider.setMajorTickSpacing(1);
        cardsUpTimeSlider.setMinorTickSpacing(1);
        cardsUpTimeSlider.setPaintTicks(true);
        JLabel cardsUpTimeLabel = new JLabel("Time cards stay up: ", JLabel.CENTER);
        Dictionary<Integer, JLabel> labels = new Hashtable<>();
        labels.put(DEFAULT_MIN_CARDS_UP_TIME, new JLabel("Hurry up!"));
        labels.put(DEFAULT_MAX_CARDS_UP_TIME, new JLabel("Calm down!"));
        cardsUpTimeSlider.setLabelTable(labels);
        cardsUpTimeSlider.setPaintLabels(true);
        cardsUpTimePanel.add(cardsUpTimeLabel);
        cardsUpTimePanel.add(cardsUpTimeSlider);
        jPanel.add(cardsUpTimePanel);

        // Dimensions and theme
        JPanel dimensionsAndTheme = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dimensionsAndTheme.setBounds(DEFAULT_RELATIVE_POS_X_DIMENSIONS_AND_THEME,
                cardsUpTimePanel.getY() + cardsUpTimePanel.getHeight() + DEFAULT_RELATIVE_POS_Y_DIMENSIONS_AND_THEME,
                DEFAULT_X_SIZE, DEFAULT_Y_SIZE_DIMENSIONS_AND_THEME);

        SpinnerModel modelX = new SpinnerNumberModel(DEFAULT_COLS, DEFAULT_MIN_COLS, DEFAULT_MAX_COLS, 1);
        SpinnerModel modelY = new SpinnerNumberModel(DEFAULT_ROWS, DEFAULT_MIN_ROWS, DEFAULT_MAX_ROWS, 1);
        sizeXSpinner = new JSpinner(modelX);
        sizeXSpinner.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        JLabel sizeXLabel = new JLabel("Columns: ", JLabel.CENTER);
        dimensionsAndTheme.add(sizeXLabel);
        dimensionsAndTheme.add(sizeXSpinner);
        sizeYSpinner = new JSpinner(modelY);
        sizeYSpinner.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        JLabel sizeYLabel = new JLabel("Rows: ", JLabel.CENTER);
        dimensionsAndTheme.add(sizeYLabel);
        dimensionsAndTheme.add(sizeYSpinner);

        themesComboBox = new JComboBox<>(Enums.Theme.getStringNames().toArray(new String[0]));
        themesComboBox.setSelectedIndex(1);
        themesComboBox.setPreferredSize(new Dimension(DEFAULT_X_SIZE_COMBO_BOX, DEFAULT_Y_SIZE_COMBO_BOX));
        JLabel themesLabel = new JLabel("Theme: ", JLabel.CENTER);
        dimensionsAndTheme.add(themesLabel);
        dimensionsAndTheme.add(themesComboBox);

        jPanel.add(dimensionsAndTheme);

        // Opponent choosing panel
        JPanel opponentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        opponentPanel.setBounds(DEFAULT_RELATIVE_POS_X_OPPONENT,
                dimensionsAndTheme.getY() + dimensionsAndTheme.getHeight() + DEFAULT_RELATIVE_POS_Y_OPPONENT,
                DEFAULT_X_SIZE, DEFAULT_Y_SIZE_OPPONENT);

        JLabel selectOpponentLabel = new JLabel("Game type: ");
        opponentPanel.add(selectOpponentLabel);

        JRadioButton onePlayerButton = new JRadioButton("One player");
        onePlayerButton.setActionCommand(MENU_ONE_PLAYER);
        onePlayerButton.setMnemonic(KeyEvent.VK_O);
        onePlayerButton.setSelected(true);
        opponentPanel.add(onePlayerButton);

        JRadioButton twoPlayersButton = new JRadioButton("Two players");
        twoPlayersButton.setActionCommand(MENU_TWO_PLAYERS);
        twoPlayersButton.setMnemonic(KeyEvent.VK_T);
        opponentPanel.add(twoPlayersButton);

        JRadioButton computerButton = new JRadioButton("Computer");
        computerButton.setActionCommand(MENU_COMPUTER);
        computerButton.setMnemonic(KeyEvent.VK_C);
        opponentPanel.add(computerButton);

        opponentRadioGroup = new ButtonGroup();
        opponentRadioGroup.add(onePlayerButton);
        opponentRadioGroup.add(twoPlayersButton);
        opponentRadioGroup.add(computerButton);

        jPanel.add(opponentPanel);

        // Lower part buttons
        JPanel lowerButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lowerButtons.setBounds(DEFAULT_RELATIVE_POS_X_LOWER_BUTTONS,
                opponentPanel.getY() + opponentPanel.getHeight() + DEFAULT_RELATIVE_POS_Y_LOWER_BUTTONS,
                DEFAULT_X_SIZE, DEFAULT_Y_SIZE_LOWER_BUTTONS);

        loadThemeButton = new JButton("Load Theme");
        loadThemeButton.setMnemonic(KeyEvent.VK_T);
        loadThemeButton.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        loadThemeButton.setActionCommand(MENU_LOAD_THEME);
        loadThemeButton.addActionListener(this);
        lowerButtons.add(loadThemeButton);

        JButton highScoresButton = new JButton("High Scores");
        highScoresButton.setMnemonic(KeyEvent.VK_H);
        highScoresButton.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        highScoresButton.setActionCommand(MENU_SHOW_HIGH_SCORES);
        highScoresButton.addActionListener(this);
        lowerButtons.add(highScoresButton);

        JButton startButton = new JButton("Start");
        startButton.setMnemonic(KeyEvent.VK_S);
        startButton.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        startButton.setActionCommand(MENU_START);
        startButton.addActionListener(this);
        lowerButtons.add(startButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setMnemonic(KeyEvent.VK_E);
        exitButton.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        exitButton.setActionCommand(MENU_EXIT);
        exitButton.addActionListener(this);
        lowerButtons.add(exitButton);

        jPanel.add(lowerButtons);

    }

    private void showAboutPage() {
        JEditorPane aboutText = new JEditorPane();
        aboutText.setContentType("text/html");
        String content = null;
        try {
            content =
                    new Scanner(new File(DEFAULT_FOLDER_RES + DEFAULT_FILENAME_ABOUT_PAGE)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException ignored) {
        }
        aboutText.setText(content);
        aboutText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setPreferredSize(new Dimension(DEFAULT_X_SIZE_ABOUT_PAGE, DEFAULT_Y_SIZE_ABOUT_PAGE));

        JOptionPane.showMessageDialog(this, scrollPane, "High Scores", JOptionPane.PLAIN_MESSAGE);
    }

    private void showHighScores() {
        ArrayList<HighScoreModel> highScores = sqLite.getHighScores();
        JOptionPane.showMessageDialog(this, new HighScoresView(highScores), "High Scores", JOptionPane.PLAIN_MESSAGE);
    }

    private void startGame() {
        int columns = (int) sizeXSpinner.getModel().getValue();
        int rows = (int) sizeYSpinner.getModel().getValue();
        int difficulty = difficultySlider.getModel().getValue();
        double cardsUpTime = ((double) cardsUpTimeSlider.getModel().getValue()) * 0.5;
        opponentRadioGroup.getSelection().getActionCommand();
        Enums.Theme theme =
                Enums.Theme.valueOf(((String) Objects.requireNonNull(themesComboBox.getSelectedItem())).toUpperCase());
        Enums.GameMode gameMode;
        switch (opponentRadioGroup.getSelection().getActionCommand()) {
            case MENU_TWO_PLAYERS:
                gameMode = Enums.GameMode.TWO_PLAYERS;
                break;
            case MENU_COMPUTER:
                gameMode = Enums.GameMode.COMPUTER;
                break;
            default:
                gameMode = Enums.GameMode.ONE_PLAYER;
                break;
        }


        if ((columns * rows) % 2 != 0) {
            JOptionPane.showMessageDialog(this, "The product of rows and columns has to be even.");
        } else {
            int numberOfCards = columns * rows / 2;
            GameParamsModel gameParams =
                    new GameParamsModel(columns, rows, difficulty, cardsUpTime, numberOfCards, gameMode);
            if (theme == Enums.Theme.CUSTOM) {
                if (customCards != null) {
                    ArrayList<CardModel> gameCards =
                            new ArrayList<>(
                                    customCards.subList(0, gameParams.getNumberOfNormalCards())
                            );
                    gameCards.addAll(FileIO.readBombCards(new File(DEFAULT_FOLDER_RES + DEFAULT_FILENAME_BOMB_CARD),
                            gameParams.getNumberOfBombCards()));
                    new GameController(new GameModel(gameParams, gameCards));
                } else {
                    JOptionPane.showMessageDialog(this, "No custom theme has been loaded.");
                }
            } else {
                ArrayList<CardModel> gameCards =
                        FileIO.readNormalCards(
                                FileIO.getDirectoryFromPath(DEFAULT_FOLDER_RES + theme.toString().toLowerCase()),
                                gameParams.getNumberOfNormalCards()
                        );
                gameCards.addAll(FileIO.readBombCards(new File(DEFAULT_FOLDER_RES + DEFAULT_FILENAME_BOMB_CARD),
                        gameParams.getNumberOfBombCards()));
                new GameController(new GameModel(gameParams, gameCards));
            }
        }
    }

    private void loadTheme() {
        //Create a file chooser
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            ArrayList<CardModel> cards = FileIO.readCustomCards(
                    fc.getSelectedFile(),
                    Objects.requireNonNull(fc.getSelectedFile().listFiles(IMG_FILTER)).length
            );

            if (cards.size() >= 20) {
                themesComboBox.setSelectedIndex(themesComboBox.getItemCount() - 1);
                themesComboBox.setEnabled(false);
                loadThemeButton.setActionCommand(MENU_REMOVE_THEME);
                loadThemeButton.setText("Remove Theme");
                customCards = cards;
                JOptionPane.showMessageDialog(this,
                        "Theme loaded successfully.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "The number of image files in the folder has to be greater than 20.");
            }
        }
    }

    private void removeTheme() {
        themesComboBox.setSelectedIndex(0);
        themesComboBox.setEnabled(true);
        customCards = null;
        loadThemeButton.setActionCommand(MENU_LOAD_THEME);
        loadThemeButton.setText("Load Theme");
        JOptionPane.showMessageDialog(this,
                "Theme removed successfully.");
    }

}
