package sk.uniba.fmph.dcs.terra_futura;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// Game class implemented as a singleton
public class Game {
    private static Game uniqueInstance;
    private static GameState gameState;
    private static List<Player> players;
    private static int onTurn; // player who is on turn - ID corresponds to index in players
    private static int turnNumber; // game ends on turn 10 (TODO: correct me if I'm wrong)
    private static GameObserver gameObserver; // TODO: initialize and notify

    private Game(int playerCount) {
        if (playerCount < 2 || playerCount > 4) {
            throw new IllegalArgumentException("Too many or too little players for game");
        }

        gameState = GameState.TakeCardNoCardDiscarded;
        onTurn = 0;
        turnNumber = 1;

        players = new ArrayList<>();

        for (int i = 0; i < playerCount; i++) {
            // TODO:
            /* once all other classes are ready,
                initialize playerCount players
                with a random starting card at the center of grid (0,0)
                two random activationPattern instances
                two random scoringMethod instances
                (getting random cards could be a static method for these classes)
                (or we can just not care because it's not important and give the same cards)
             */
            players.add(new Player( /* .... */ ));

        }
    }

    public static void startNewGame(int playerCount) {
        uniqueInstance = new Game(playerCount);
    }

    public static Game getUniqueInstance() {
        if (uniqueInstance == null) {
            throw new NoSuchElementException("No new game has been started");
        }

        return uniqueInstance;
    }

    public int getOnTurn() {
        return onTurn;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

}
