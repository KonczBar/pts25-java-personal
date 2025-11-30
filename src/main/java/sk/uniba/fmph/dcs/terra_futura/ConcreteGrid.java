package sk.uniba.fmph.dcs.terra_futura;

import java.util.Optional;

public class ConcreteGrid implements Grid {
    @Override
    public Optional<Card> getCard(GridPosition coordinate) {
        return Optional.empty();
    }

    @Override
    public boolean canPutCard(GridPosition coordinate) {
        return false;
    }

    @Override
    public void putCard(GridPosition coordinate, Card card) {

    }

    @Override
    public boolean canBeActivated(GridPosition coordinate) {
        return false;
    }

    @Override
    public void setActivated(GridPosition coordinate) {

    }

    @Override
    public void setActivationPattern(ActivationPattern pattern) {

    }

    @Override
    public void endTurn() {

    }

    @Override
    public String state() {
        return "";
    }
}
