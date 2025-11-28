package sk.uniba.fmph.dcs.terra_futura;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// Game class
public class Game {

    private TerraFuturaInterface tfi;
    private GameState gameState;
    private int playerCount;
    private int onTurn; // player who is on turn - ID corresponds to index in players
    private int turnNumber; // game ends on turn 10 (TODO: correct me if I'm wrong)
    private Optional<Integer> assistingPlayer;

    private Game(int playerCount, TerraFuturaInterface tfi) {
        if (playerCount < 2 || playerCount > 4) {
            throw new IllegalArgumentException("Too many or too little players for game");
        }

        this.tfi = tfi;
        this.playerCount = playerCount;
        gameState = GameState.TakeCardNoCardDiscarded;
        onTurn = 1;
        turnNumber = 1;
        assistingPlayer = Optional.empty();
    }

    private boolean isOnTurn(int playerID) {
        return playerID == onTurn;
    }

    public boolean takeCard(int playerID, CardSource source, GridPosition destination) {
        if (isOnTurn(playerID) &&
                (gameState == GameState.TakeCardNoCardDiscarded || gameState == GameState.TakeCardCardDiscarded)) {
            if (tfi.takeCard(playerID, source, destination)) {
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
                    return false; // non-existent assisting player
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

                    assistingPlayer = Optional.empty();
                    gameState = GameState.ActivateCard;
                    return true;
                }
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

        return false;
    }
}
