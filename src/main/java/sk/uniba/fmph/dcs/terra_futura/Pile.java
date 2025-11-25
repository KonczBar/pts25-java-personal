package sk.uniba.fmph.dcs.terra_futura;

import java.util.Optional;

public interface Pile {
    Optional<Card> getCard(int index);
    void takeCard(int index);
    void removeLastCard();
    String state();
}
