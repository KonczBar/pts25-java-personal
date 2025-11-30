package sk.uniba.fmph.dcs.terra_futura;

import java.util.Optional;

public class MoveCard {
    private final Pile pile;
    private final Grid grid;

    public MoveCard(Pile pile, Grid grid) {
        this.pile = pile;
        this.grid = grid;
    }

    public boolean moveCard(int index, GridPosition coordinate) {
        if(!grid.canPutCard(coordinate)) return false;

        Optional<Card> optionalCard = pile.getCard(index);

        if(optionalCard.isEmpty()) return false;

        Card card = optionalCard.get();
        grid.putCard(coordinate, card);

        return true;
    }
}
