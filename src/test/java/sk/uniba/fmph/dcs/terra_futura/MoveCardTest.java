package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

class GridFake implements Grid {
    private GridPosition startingCardCoordinates = null;
    private final int rightConstraint = 2;
    private final List<List<Card>> cards = new ArrayList<>();

    public GridFake() {
        for(int i = 0; i <= rightConstraint; i++) {
            List<Card> arr = new ArrayList<>();

            for(int j = 0; j <= rightConstraint; j++) {
                arr.add(null);
            }

            cards.add(arr);
        }
    }

    @Override
    public boolean canPutCard(GridPosition coordinate) {
        if(startingCardCoordinates == null) return true;
        int x = startingCardCoordinates.getX() + coordinate.getX();
        int y = startingCardCoordinates.getY() + coordinate.getY();

        if(x > rightConstraint || x < 0 || y > rightConstraint || y < 0) {
            return false;
        }

        return cards.get(x).get(y) == null;
    }

    @Override
    public void putCard(GridPosition coordinate, Card card) {
        int x, y;

        if(startingCardCoordinates == null) {
            if(coordinate.getX() < 0) {
                x = coordinate.getX() + rightConstraint;
            } else {
                x = coordinate.getX();
            }

            if(coordinate.getY() < 0) {
                y = coordinate.getY() + rightConstraint;
            } else {
                y = coordinate.getY();
            }

            //[-2, -2] = [0, 0], [-1, -1] = [1, 1]

            cards.get(x).set(y, card);
            startingCardCoordinates = new GridPosition(x, y);
        } else {
            x = startingCardCoordinates.getX() + coordinate.getX();
            y = startingCardCoordinates.getY() + coordinate.getY();
            if(!canPutCard(coordinate)) throw new InvalidMoveException("Coordinates [" + x + ", " + y
                    + "] are out of bounds or there is already another card.");
            cards.get(x).set(y, card);
        }
    }

    @Override
    public Optional<Card> getCard(GridPosition coordinate) {return Optional.empty();}
    @Override
    public boolean canBeActivated(GridPosition coordinate) {return false;}
    @Override
    public void setActivated(GridPosition coordinate) {}
    @Override
    public void setActivationPattern(ActivationPattern pattern) {}
    @Override
    public void endTurn() {}
    @Override
    public String state() {return "fake grid";}
}

class PileFake implements Pile {
    private final int n = 5;
    private final List<Card> cards = new ArrayList<>(n);

    public PileFake() {
        for(int i = 0; i < n; i++) {
            cards.add(new Card(0));
        }
    }

    @Override
    public Optional<Card> getCard(int index) {
        if(index < 0 || index >= n) {
            return Optional.empty();
        }
        return Optional.of(cards.get(index));
    }

    @Override
    public void takeCard(int index) {}
    @Override
    public void removeLastCard() {}
    @Override
    public String state() {
        return "fake pile";
    }
}

public class MoveCardTest {
    private MoveCard moveCard;

    @Before
    public void setUp() {
        Grid grid = new GridFake();
        Pile pile = new PileFake();
        moveCard = new MoveCard(pile, grid);
    }

    @Test
    public void testOutOfBounds() {
        assertTrue(moveCard.moveCard(3, new GridPosition(-2, -2)));
        // startingCard - [0, 0], can't move to the left or down
        assertFalse(moveCard.moveCard(4, new GridPosition(-1, -1)));
    }

    @Test
    public void testPlacingOnUnoccupiedSpace() {
        assertTrue(moveCard.moveCard(0, new GridPosition(-2, -2)));
        assertTrue(moveCard.moveCard(4, new GridPosition(1, 1)));
    }

    @Test
    public void testPlacingOnOccupiedSpace() {
        assertTrue(moveCard.moveCard(0, new GridPosition(-1, -1)));
        //startingCard - [1, 1]

        assertFalse(moveCard.moveCard(1, new GridPosition(0, 0)));
        assertTrue(moveCard.moveCard(2, new GridPosition(1, 1)));
        assertFalse(moveCard.moveCard(3, new GridPosition(1, 1)));
    }

    @Test
    public void testNonExistentCard() {
        assertFalse(moveCard.moveCard(5, new GridPosition(0, 0)));
    }
}
