package util;

import constants.Enums;
import model.game.GameModel;
import model.game.HighScoreModel;

import java.sql.*;
import java.util.ArrayList;

public class SQLite {

    private Connection conn;

    public SQLite() {
        init();
    }

    private void init() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("No SQLite class found");
        }
        String url = "bp.sqlite";
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + url);
        } catch (SQLException e) {
            System.out.println("Error when connecting to database.");
        }
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS high_scores (" +
                "id integer PRIMARY KEY AUTOINCREMENT, " +
                "score integer NOT NULL, " +
                "elapsed_time FLOAT NOT NULL, " +
                "game_mode TEXT NOT NULL, " +
                "date TEXT NOT NULL" +
                ");";
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
            conn.commit();
        } catch (SQLException e) {
            System.out.println("Error when creating the table");
        }
    }

    public void insertHighScores(GameModel gameModel, float elapsedTime) {
        if (gameModel.getGameParams().getGameMode() == Enums.GameMode.TWO_PLAYERS) {
            insertOneHighScore(gameModel.getPlayerModels()[0].getScore(), elapsedTime, gameModel.getGameParams().getGameMode());
            insertOneHighScore(gameModel.getPlayerModels()[1].getScore(), elapsedTime, gameModel.getGameParams().getGameMode());
        } else {
            insertOneHighScore(gameModel.getPlayerModels()[0].getScore(), elapsedTime, gameModel.getGameParams().getGameMode());
        }
    }

    public void insertOneHighScore(int score, float elapsedTime, Enums.GameMode gameMode) {
        try {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO high_scores " +
                    "(score, elapsed_time, game_mode, date) " +
                    "VALUES " +
                    "(?, ?, ?, CURRENT_TIMESTAMP);";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, score);
            statement.setFloat(2, elapsedTime);
            statement.setString(3, gameMode.toString());
            statement.executeUpdate();
            statement.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error when saving the high score to the database.");
        }
    }

    public ArrayList<HighScoreModel> getHighScores() {
        ArrayList<HighScoreModel> highScores = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM high_scores " +
                    "ORDER BY " +
                    "score DESC, " +
                    "elapsed_time ASC " +
                    "LIMIT 20" +
                    ";";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                HighScoreModel highScore = new HighScoreModel(
                        rs.getInt("id"),
                        rs.getInt("score"),
                        rs.getFloat("elapsed_time"),
                        Enums.GameMode.valueOf(rs.getString("game_mode")),
                        rs.getString("date")
                );
                highScores.add(highScore);
            }
            rs.close();
            stmt.close();
            conn.commit();
            return highScores;
        } catch (SQLException e) {
            System.out.println("Error when retrieving the high scores.");
        }
        return highScores;
    }
}