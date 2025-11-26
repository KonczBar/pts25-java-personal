package sk.uniba.fmph.dcs.terra_futura;

import java.util.List;
import java.util.Optional;

public interface Grid {
      Optional<Card> getCard(GridPosition coordinate);
      boolean canPutCard(GridPosition coordinate);
      void putCard(GridPosition coordinate, Card card);
      boolean canBeActivated(GridPosition coordinate);
      void setActivated(GridPosition coordinate);
      void setActivationPattern(List<GridPosition> pattern);
      void endTurn();
      String state();
}