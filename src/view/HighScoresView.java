package view;

import constants.Enums;
import model.HighScoreModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static constants.Const.*;
import static constants.Strings.HIGH_SCORES_COLUMN_NAMES;

public class HighScoresView extends JTabbedPane {

    public HighScoresView(ArrayList<HighScoreModel> highScores) {
        init(highScores);
    }

    private void init(ArrayList<HighScoreModel> highScores) {

        setPreferredSize(new Dimension(DEFAULT_X_SIZE_HIGH_SCORES, DEFAULT_Y_SIZE_HIGH_SCORES));

        JComponent panel1 = makeHighScoresPanel(highScores, Enums.GameMode.ONE_PLAYER);
        addTab("One Player", panel1);
        setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = makeHighScoresPanel(highScores, Enums.GameMode.TWO_PLAYERS);
        addTab("Two players", panel2);
        setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel3 = makeHighScoresPanel(highScores, Enums.GameMode.COMPUTER);
        addTab("Player vs. Computer", panel3);
        setMnemonicAt(2, KeyEvent.VK_3);

    }

    private JComponent makeHighScoresPanel(ArrayList<HighScoreModel> highScores, Enums.GameMode gameMode) {
        Object[][] data = new Object[highScores.size()][4];
        int j = 0;
        for (int i = 0; i < 20 && i < highScores.size(); i++) {
            if (highScores.get(i).getGame_mode() != gameMode) {
                continue;
            }
            data[j][0] = j + 1;
            data[j][1] = highScores.get(i).getScore();
            data[j][2] = highScores.get(i).getElapsed_time();
            data[j][3] = highScores.get(i).getDate();
            j++;
        }

        NonEditableTable table = new NonEditableTable(data, HIGH_SCORES_COLUMN_NAMES);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(NonEditableTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(DEFAULT_X_SIZE_HIGH_SCORES, DEFAULT_Y_SIZE_HIGH_SCORES));

        return scrollPane;
    }

    private static class NonEditableTable extends JTable {

        NonEditableTable(Object[][] rowData, Object[] columnNames) {
            super(rowData, columnNames);
            getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

}

