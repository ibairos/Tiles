package controller;

import model.BombCardModel;
import model.CardModel;
import model.ComputerPlayerModel;
import model.GameModel;
import util.FileIO;
import util.SQLite;
import view.BombIconView;
import view.CardView;
import view.ScoreView;
import view.TurnView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static constants.Const.*;
import static constants.Strings.DEFAULT_FILENAME_TURN;
import static constants.Strings.DEFAULT_FOLDER_RES;

public class GameController extends TilesController {

    private static final String FRAME_TITLE = "Tiles - Game";

    private GameModel gameModel;

    private JPanel jPanel;

    private ArrayList<CardView> cards;

    private ArrayList<CardModel> faceUpCards;

    private BombIconView bomb;

    private SQLite sqLite;

    private long startTime;

    GameController(GameModel gameModel) throws HeadlessException {
        super(FRAME_TITLE, gameModel.getSizeX(), gameModel.getSizeY());
        this.gameModel = gameModel;
        init();
    }

    @Override
    public void init() {
        faceUpCards = new ArrayList<>();
        sqLite = new SQLite();
        setupJPanel();
        add(jPanel);
        setVisible(true);
    }

    @Override
    public void destroy() {
        // It does nothing in this implementation
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("exit")) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        destroy();
    }

    private void setupJPanel() {
        // Initialize JPanel
        jPanel = new JPanel(null);

        // Upper panel
        JPanel upperPanel = new JPanel(null);
        upperPanel.setBounds(DEFAULT_INITIAL_POS_X_UPPER, DEFAULT_INITIAL_POS_Y_UPPER, gameModel.getSizeX(),
                DEFAULT_Y_SIZE_UPPER);
        upperPanel.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        upperPanel.setBackground(Color.GRAY);

        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setBounds(DEFAULT_INITIAL_POS_X_SCORES, DEFAULT_INITIAL_POS_Y_SCORES, DEFAULT_X_SIZE_SCORES,
                DEFAULT_Y_SIZE_SCORES);
        scoresPanel.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        scoresPanel.setBackground(Color.GRAY);

        JLabel scoresTitleLabel = new JLabel("SCORES");
        scoresTitleLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
        scoresPanel.add(scoresTitleLabel);
        ScoreView[] scoreViews = new ScoreView[2];

        switch (gameModel.getGameParams().getGameMode()) {
            case TWO_PLAYERS:
                scoreViews[0] = new ScoreView(gameModel, "Player 1", 0);
                scoresPanel.add(scoreViews[0]);
                scoreViews[1] = new ScoreView(gameModel, "Player 2", 1);
                scoresPanel.add(scoreViews[1]);
                break;
            case COMPUTER:
                scoreViews[0] = new ScoreView(gameModel, "Player", 0);
                scoresPanel.add(scoreViews[0]);
                scoreViews[1] = new ScoreView(gameModel, "Computer", 1);
                scoresPanel.add(scoreViews[1]);
                break;
            default:
                scoreViews[0] = new ScoreView(gameModel, "Player", 0);
                scoresPanel.add(scoreViews[0]);
                break;
        }

        upperPanel.add(scoresPanel);

        upperPanel.add(new TurnView(FileIO.readTurnIcon(new File(DEFAULT_FOLDER_RES + DEFAULT_FILENAME_TURN)),
                gameModel, scoresPanel));

        JButton exitButton = new JButton("Exit");
        exitButton.setMnemonic(KeyEvent.VK_E);
        exitButton.setActionCommand("exit");
        exitButton.addActionListener(this);
        exitButton.setBounds(gameModel.getSizeX() - DEFAULT_X_SIZE_EXIT_BUTTON - DEFAULT_SIZE_BORDER,
                (DEFAULT_Y_SIZE_UPPER - DEFAULT_Y_SIZE_EXIT_BUTTON) / 2,
                DEFAULT_X_SIZE_EXIT_BUTTON, DEFAULT_Y_SIZE_EXIT_BUTTON);
        exitButton.setBorder(new EmptyBorder(DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER, DEFAULT_SIZE_BORDER,
                DEFAULT_SIZE_BORDER));
        upperPanel.add(exitButton);

        jPanel.add(upperPanel);

        JPanel gridPanel = new JPanel(new GridLayout(gameModel.getGameParams().getRows(),
                gameModel.getGameParams().getColumns()));

        gridPanel.setBounds(0, 0, gameModel.getSizeX(),
                gameModel.getSizeY() - upperPanel.getHeight() - 4 * DEFAULT_SIZE_BORDER);

        int i = 0;
        cards = new ArrayList<>();
        for (CardModel card : gameModel.getCardModels()) {
            CardView cardView = new CardView(card, i);
            cardView.addMouseListener(new CardClickListener(i));
            cards.add(cardView);
            gridPanel.add(cardView);
            i++;
        }

        JLayeredPane jLayeredPane = new JLayeredPane();
        jLayeredPane.setBounds(upperPanel.getX(), upperPanel.getY() + upperPanel.getHeight(), gameModel.getSizeX(),
                gameModel.getSizeY() - upperPanel.getHeight() - 4 * DEFAULT_SIZE_BORDER);
        jLayeredPane.add(gridPanel);

        bomb = new BombIconView(jLayeredPane);
        jLayeredPane.add(bomb);
        jPanel.add(jLayeredPane);

    }

    private void checkCard(CardModel cardModel) {
        gameModel.updateComputerCardMap(cards);
        if (cardModel.getClass() == BombCardModel.class) {
            gameModel.modifyScore(DEFAULT_POINTS_BOMB);
            if (faceUpCards.size() > 0) {
                faceUpCards.get(0).flip();
                faceUpCards.clear();
            }
            cardModel.setCorrect();
            new Thread(bomb).start();
            gameModel.changeTurn();
            if (gameModel.isComputersTurn()) {
                SwingUtilities.invokeLater(this::computersTurn);
            }
        } else {
            if (faceUpCards.size() == 0) {
                faceUpCards.add(cardModel);
            } else {
                CardModel faceUpCard = faceUpCards.get(0);
                if (cardModel.getCardNumber() == faceUpCard.getCardNumber()) {
                    cardModel.setCorrect();
                    faceUpCard.setCorrect();
                    gameModel.modifyScore(DEFAULT_POINTS_TILE_OK);
                    if (gameModel.isGameEnded()) {
                        endGame();
                    }
                } else {
                    cardModel.flip();
                    faceUpCard.flip();
                    try {
                        Thread.sleep((long) (gameModel.getGameParams().getCardsUpTime() * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    gameModel.changeTurn();
                }
                faceUpCards.clear();
                if (gameModel.isComputersTurn()) {
                    SwingUtilities.invokeLater(this::computersTurn);
                }
            }
        }
        repaint();
    }

    private void endGame() {
        long endTime = System.currentTimeMillis();
        float elapsedTime = ((float) (endTime - startTime)) / 1000;
        sqLite.insertHighScores(gameModel, elapsedTime);
        JLabel scoreInfo;
        switch (gameModel.getGameParams().getGameMode()) {
            case TWO_PLAYERS:
                scoreInfo = new JLabel("Score Player 1: " + gameModel.getPlayerModels()[0].getScore() + ", Score " +
                        "Player 2: " + gameModel.getPlayerModels()[1].getScore() + ", Elapsed time: " + elapsedTime + "s.",
                        JLabel.CENTER);
                break;
            case COMPUTER:
                scoreInfo = new JLabel("Score Player: " + gameModel.getPlayerModels()[0].getScore() + ", Score " +
                        "Computer : " + gameModel.getPlayerModels()[1].getScore() + ", Elapsed time: " + elapsedTime + "s.",
                        JLabel.CENTER);
                break;
            default:
                scoreInfo = new JLabel(
                        "Score: " + gameModel.getPlayerModels()[0].getScore() + ", Elapsed time: " + elapsedTime + "s" +
                                ".", JLabel.CENTER);
                break;
        }

        JOptionPane.showMessageDialog(this, scoreInfo, "Game completed!", JOptionPane.INFORMATION_MESSAGE);
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void computersTurn() {
        new Thread(new ComputerMoves()).start();
    }

    private void cardPicked(int position) {
        cards.get(position).getCardModel().flip();
        repaint();

        SwingUtilities.invokeLater(() -> {
            checkCard(cards.get(position).getCardModel());
        });
    }

    private class CardClickListener implements MouseListener {

        private int position;

        CardClickListener(int position) {
            this.position = position;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }

            if (!gameModel.isComputersTurn() && !cards.get(position).getCardModel().isFacingUp()) {
                cardPicked(position);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class ComputerMoves implements Runnable {
        @Override
        public void run() {
            int[] picks = ((ComputerPlayerModel) gameModel.getPlayerModels()[1]).selectCards(cards);
            try {
                Thread.sleep(COMPUTER_WAIT_TIME_MILLIS);
            } catch (InterruptedException ignored) {
            }
            try {
                SwingUtilities.invokeAndWait(() -> {
                    cardPicked(picks[0]);
                    repaint();
                });
            } catch (InterruptedException | InvocationTargetException ignored) {

            }

            try {
                Thread.sleep(COMPUTER_WAIT_TIME_MILLIS);
            } catch (InterruptedException ignored) {
            }

            try {
                SwingUtilities.invokeAndWait(() -> {
                    if (gameModel.isComputersTurn()) {
                        cardPicked(picks[1]);
                    }
                });
            } catch (InterruptedException | InvocationTargetException ignored) {
            }
        }
    }

}
