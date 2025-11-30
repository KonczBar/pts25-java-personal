package sk.uniba.fmph.dcs.terra_futura;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

// Game class
public class Game {

    private TerraFuturaInterface tfi;
    private GameState gameState;
    private int playerCount;
    private int onTurn; // player who is on turn - ID corresponds to index in players
    private int turnNumber; // game ends on turn 10 (TODO: correct me if I'm wrong)
    private Optional<Integer> assistingPlayer;
    private final GameObserver gameObserver;
    private final Map<Integer, String> messageToPlayers;

    private Game(int playerCount, TerraFuturaInterface tfi) {
        if (playerCount < 2 || playerCount > 4) {
            throw new IllegalArgumentException("Too many or too little players for game");
        }

        this.tfi = tfi;
        this.playerCount = playerCount;
        Map<Integer, Observer> observers = new HashMap<>();
        messageToPlayers = new HashMap<>();
        for (int i = 0; i < playerCount; i++){
            observers.put(i, new IdObserver(i));
        }
        gameObserver = new GameObserver(observers);
        gameState = GameState.TakeCardNoCardDiscarded;
        onTurn = 1;
        turnNumber = 1;
        assistingPlayer = Optional.empty();
    }

    private boolean isOnTurn(int playerID) {
        return playerID == onTurn;
    }

    public boolean takeCard(int playerId, CardSource source, GridPosition destination) {
        if (!isOnTurn(playerId)){
            messageToPlayers.clear();
            messageToPlayers.put(playerId,"Incorrect player id");
            gameObserver.notifyAll(messageToPlayers);
            return false;
        }
        if (gameState == GameState.TakeCardNoCardDiscarded || gameState == GameState.TakeCardCardDiscarded) {
            if (tfi.takeCard(playerId, source, destination)) {
                gameState = GameState.ActivateCard;
                return true;
            }
        }


        return false;
    }


    public boolean discardLastCardFromDeck(int playerId, Deck deck) {
        if (isOnTurn(playerId) && gameState == GameState.TakeCardNoCardDiscarded) {
            if (tfi.discardLastCardFromDeck(playerId, deck)) {
                gameState = GameState.TakeCardCardDiscarded;
                return true;
            }
        }
        return false;
    }


    // briefly go to selectreward if assistance is used
    public boolean activateCard(int playerId, GridPosition gridPosition, List<Pair<Resource, GridPosition>> inputs,
                                List<Pair<Resource, GridPosition>> outputs, List<GridPosition> pollution,
                                Optional<Integer> otherPlayerId, Optional<GridPosition> otherCard) {

        if (isOnTurn(playerId) && gameState == GameState.ActivateCard) {

            // checking assisting player
            if (otherPlayerId.isPresent()) {
                if (otherPlayerId.get() < 1 || otherPlayerId.get() > playerCount) {
                    messageToPlayers.clear();
                    messageToPlayers.put(playerId,"Incorrect assisting player id");
                    gameObserver.notifyAll(messageToPlayers);

                    return false;
                } else {
                    assistingPlayer = otherPlayerId;
                }
            }

            // if assistance was used, different state
            if (tfi.activateCard(playerId, gridPosition, inputs, outputs, pollution, otherPlayerId, otherCard)) {
                if (assistingPlayer.isPresent()) {
                    gameState = GameState.SelectReward;
                } else {
                    gameState = GameState.ActivateCard;
                }
                return true;
            }
        }

        return false;
    }


    // return to onturn player afterwards
    public boolean selectReward(int playerId, Resource resource) {
        if (assistingPlayer.isPresent()) {
            if (assistingPlayer.get() == playerId && gameState == GameState.SelectReward) {
                if (tfi.selectReward(playerId, resource)) {

                    messageToPlayers.clear();
                    messageToPlayers.put(playerId,"Reward received");
                    gameObserver.notifyAll(messageToPlayers);

                    assistingPlayer = Optional.empty();
                    gameState = GameState.ActivateCard;
                    return true;
                }
                messageToPlayers.clear();
                messageToPlayers.put(playerId,"Incorrect resources");
                gameObserver.notifyAll(messageToPlayers);
                return false;
            }
        }

        return false;
    }

    public boolean turnFinished(int playerId) {
        if (isOnTurn(playerId) && (gameState == GameState.ActivateCard)) {
            if (tfi.turnFinished(playerId)) {
                if (onTurn == playerCount) {
                    turnNumber++;
                }

                onTurn = (onTurn % playerCount) + 1;

                if (turnNumber == 10) {
                    gameState = GameState.SelectActivationPattern;
                } else {
                    gameState = GameState.TakeCardNoCardDiscarded;
                }

                return true;
            }
        }
        return false;
    }


    public boolean selectActivationPattern(int playerId, int card) {
        if (isOnTurn(playerId) && gameState == GameState.SelectActivationPattern) {
            if (tfi.selectActivationPattern(playerId, card)) {
                gameState = GameState.SelectScoringMethod;
                return true;
            }
        }
        messageToPlayers.clear();
        messageToPlayers.put(playerId,"Incorrect activation pattern");
        gameObserver.notifyAll(messageToPlayers);
        return false;
    }


    public boolean selectScoring(int playerId, int card) {
        if (isOnTurn(playerId) && gameState == GameState.SelectScoringMethod) {
            if (tfi.selectScoring(playerId, card)) {
                onTurn = (onTurn % playerCount) + 1;

                if (onTurn == playerCount) {
                    gameState = GameState.Finish;
                } else {
                    gameState = GameState.SelectActivationPattern;
                }

                return true;
            }
        }

        messageToPlayers.clear();
        messageToPlayers.put(playerId,"Incorrect scoring method");
        gameObserver.notifyAll(messageToPlayers);
        return false;
    }
}
