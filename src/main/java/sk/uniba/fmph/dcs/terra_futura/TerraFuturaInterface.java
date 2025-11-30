package sk.uniba.fmph.dcs.terra_futura;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Optional;


public interface TerraFuturaInterface {
    boolean takeCard(int playerID, CardSource source, GridPosition destination);
    boolean discardLastCardFromDeck(int playerId, Deck deck);
    boolean activateCard(int playerId,
                 GridPosition gridPosition,
                 List<Pair<Resource, GridPosition>> inputs,
                 List<Pair<Resource, GridPosition>> outputs,
                 List<GridPosition> pollution,
                 Optional<Integer> otherPlayerId,
                 Optional<GridPosition> otherCard);
    boolean selectReward(int playerId, Resource resource);
    boolean turnFinished(int playerId);
    boolean selectActivationPattern(int playerId, int card);
    boolean selectScoring(int playerId, int card);
}
