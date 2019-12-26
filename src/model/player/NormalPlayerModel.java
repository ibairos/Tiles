package model.player;

public class NormalPlayerModel extends PlayerModel {

    public NormalPlayerModel(String playerName) {
        super(playerName);
    }

    public NormalPlayerModel(String playerName, int difficulty) {
        super(playerName);
        setScore(5 * difficulty);
    }

}
