package constants;

import java.util.ArrayList;

public class Enums {

    public enum CardState {
        FACING_UP, FACING_DOWN, CORRECT
    }

    public enum Turn {
        PLAYER_ONE, PLAYER_TWO
    }

    public enum Theme {
        MOVIES, TV_SHOWS, MUSIC, CUSTOM;

        public static ArrayList<String> getStringNames() {
            ArrayList<String> names = new ArrayList<>();
            for (Theme theme : values()) {
                names.add(theme.toString().toLowerCase());
            }
            return names;
        }
    }

    public enum GameMode {
        ONE_PLAYER, TWO_PLAYERS, COMPUTER
    }

    public enum GameState {
        NOT_STARTED, STARTED, FINISHED
    }

}
