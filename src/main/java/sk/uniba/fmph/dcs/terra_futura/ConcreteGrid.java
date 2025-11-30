package sk.uniba.fmph.dcs.terra_futura;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.math.NumberUtils.max;
import static org.apache.commons.lang3.math.NumberUtils.min;

public class ConcreteGrid implements Grid {
    List<List<Optional<Card>>> cardMatrix;
    List<List<Boolean>> activatable;
    int cardCount = 0;
    int minX = 2;
    int maxX = 2;
    int minY = 2;
    int maxY = 2;
    boolean onGoingActivation = false; // set to true by placing card, set to false by ending turn

    public ConcreteGrid() {
        cardMatrix = new ArrayList<>();
        activatable = new ArrayList<>();

        // initializes empty grid
        for (int i = 0; i < 5; i++) {
            cardMatrix.add(new ArrayList<>());
            activatable.add(new ArrayList<>());

            for (int j = 0; j < 5; j++) {
                cardMatrix.get(i).add(Optional.empty());
                activatable.get(i).add(false);
            }
        }
    }

    private void resetActivation() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                activatable.get(i).set(j, false);
            }
        }
    }

    private boolean hasAdjacentCard(GridPosition coordinate) {
        int x = coordinate.getX() + 2;
        int y = coordinate.getY() + 2;

        return cardExists(x - 1, y) ||
                cardExists(x + 1, y) ||
                cardExists(x, y - 1) ||
                cardExists(x, y + 1);
    }

    private boolean cardExists(int x, int y) {
        if (x < 0 || x > 4 || y < 0 || y > 4) {
            return false;
        }

        return cardMatrix.get(x).get(y).isPresent();
    }

    @Override
    public Optional<Card> getCard(GridPosition coordinate) {
        return cardMatrix.get(coordinate.getX() + 2).get(coordinate.getY() + 2);
    }


    @Override
    public boolean canPutCard(GridPosition coordinate) {
        // false if a card has been placed but player has yet to end turn
        if (onGoingActivation) {
            return false;
        }

        // first card has to be at 0, 0
        if (cardCount == 0 && (coordinate.getY() != 0 || coordinate.getX() != 0)) {
            return false;
        }

        // cards placed after first card need to be placed next to an existing card
        if (!hasAdjacentCard(coordinate) && cardCount != 0) {
            return false;
        }

        // cannot place card where one has already been placed
        if (cardMatrix.get(coordinate.getX() + 2).get(coordinate.getY() + 2).isPresent()) {
            return false;
        }

        // cannot place card outside 3 x 3 internal grid
        if (coordinate.getX() + 2 < minX) {
            if (maxX - (coordinate.getX() + 2) > 2) {
                return false;
            }
        }

        if (coordinate.getX() + 2 > maxX) {
            if ((coordinate.getX() + 2) - minX > 2) {
                return false;
            }
        }

        if (coordinate.getY() + 2 < minY) {
            if (maxY - (coordinate.getY() + 2) > 2) {
                return false;
            }
        }

        if (coordinate.getY() + 2 > maxY) {
            if ((coordinate.getY() + 2) - minY > 2) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void putCard(GridPosition coordinate, Card card) {
        if (!canPutCard(coordinate)) {
            throw new IllegalArgumentException("Attempted to place card in invalid position or without ending turn");
        }

        cardMatrix.get(coordinate.getX() + 2).set(coordinate.getY() + 2, Optional.of(card));
        onGoingActivation = true;
        cardCount++;

        // cards now activatable in cross pattern
        for (int i = 0; i < 5; i++) {
            activatable.get(coordinate.getX() + 2).set(i, true);
            activatable.get(i).set(coordinate.getY() + 2, true);
        }

        maxX = max(maxX, coordinate.getX() + 2);
        minX = min(minX, coordinate.getX() + 2);
        maxY = max(maxY, coordinate.getY() + 2);
        minY = min(minY, coordinate.getY() + 2);

        if (maxX - minX > 2 || maxY - minY > 2) {
            throw new RuntimeException("Cards occupy too much space:\n" + state());
        }
    }

    @Override
    public boolean canBeActivated(GridPosition coordinate) {
        // returns true if the card is in a row/column where a card has been placed
        // AND a card is present at the coordinate
        return (activatable.get(coordinate.getX() + 2).get(coordinate.getY() + 2) &&
                cardMatrix.get(coordinate.getX() + 2).get(coordinate.getY() + 2).isPresent());
    }

    @Override
    public void setActivated(GridPosition coordinate) {
        if (!canBeActivated(coordinate)) {
            throw new IllegalArgumentException("This card cannot be activated");
        }

        activatable.get(coordinate.getX() + 2).set(coordinate.getY() + 2, false);
    }

    // does not check whether pattern activation happens on correct turn
    // that is the responsibility of Game
    @Override
    public void setActivationPattern(ActivationPattern pattern) {
        if (onGoingActivation) {
            throw new RuntimeException("Attempted to set activation pattern without ending previous turn");
        }

        // assumes that activation pattern is stored as coordinates with values -1, 0, 1
        for (AbstractMap.SimpleEntry<Integer, Integer> x : pattern.getPattern()) {
            activatable.get(x.getKey() + minX + 1).set(x.getValue() + minY + 1, true);
        }
        onGoingActivation = true;
    }

    @Override
    public void endTurn() {
        onGoingActivation = false;
        resetActivation();
    }

    @Override
    public String state() {
        StringBuilder s = new StringBuilder();
        s.append("Card matrix:\n");
        for (List<Optional<Card>> matrix : cardMatrix) {
            s.append(matrix.toString());
            s.append('\n');
        }
        s.append("Activatable matrix:\n");
        for (List<Boolean> booleans : activatable) {
            s.append(booleans.toString());
            s.append('\n');
        }

        return s.toString();
    }
}
