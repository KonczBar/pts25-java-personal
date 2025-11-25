package sk.uniba.fmph.dcs.terra_futura;

import java.util.List;
import java.util.Optional;

public interface Grid {
    Optional<Card> getCard(GridPosition coordinate);
}