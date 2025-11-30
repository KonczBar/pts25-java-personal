package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GridTest {
    private Card blankCard;
    private GridPosition startPos;

    @Before
    public void setUp() {
        blankCard = Mockito.mock(Card.class);
        startPos = Mockito.mock(GridPosition.class);
        Mockito.when(startPos.getX()).thenReturn(0);
        Mockito.when(startPos.getY()).thenReturn(0);
    }

    // checks that first card can only be placed in middle
    @Test
    public void firstCardTest() {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                GridPosition gp = Mockito.mock(GridPosition.class);
                Mockito.when(gp.getX()).thenReturn(i);
                Mockito.when(gp.getY()).thenReturn(j);
                Grid grid = new ConcreteGrid();

                if (i != 0 || j != 0) {
                    assertFalse(grid.canPutCard(gp));
                    assertThrows(IllegalArgumentException.class, () -> grid.putCard(gp, blankCard));
                } else {
                    assertTrue(grid.canPutCard(gp));
                    assertDoesNotThrow(() -> grid.putCard(gp, blankCard));
                }
            }
        }
    }

    // tests that card cannot be put down after another without calling endTurn in between
    @Test
    public void ongoingActivationPutTest() {
        Grid grid = new ConcreteGrid();
        GridPosition gp2 = Mockito.mock(GridPosition.class);
        Mockito.when(gp2.getX()).thenReturn(0);
        Mockito.when(gp2.getY()).thenReturn(1);

        grid.putCard(startPos, blankCard);
        assertFalse(grid.canPutCard(gp2));
        assertThrows(IllegalArgumentException.class, ()-> grid.putCard(gp2, blankCard));
        grid.endTurn();
        assertTrue(grid.canPutCard(gp2));
        assertDoesNotThrow(()->grid.putCard(gp2, blankCard));
    }

    // tests that only adjacent cards can be placed
    @Test
    public void adjacentCardTest() {
        Grid grid = new ConcreteGrid();
        grid.putCard(startPos, blankCard);
        grid.endTurn();

        GridPosition invalid = Mockito.mock(GridPosition.class);
        Mockito.when(invalid.getX()).thenReturn(0);
        Mockito.when(invalid.getY()).thenReturn(-2);

        assertFalse(grid.canPutCard(invalid));
        assertThrows(IllegalArgumentException.class, ()-> grid.putCard(invalid, blankCard));

        // testing valid position is redundant
    }

    // tests that a position can only accept a card once
    @Test
    public void doublePlaceTest() {
        Grid grid = new ConcreteGrid();
        grid.putCard(startPos, blankCard);
        grid.endTurn();
        assertFalse(grid.canPutCard(startPos));
    }

    // tests that placed cards cannot fall outside the 3 x 3 internal grid
    @Test
    public void outOfRangeTest() {
        Grid grid = new ConcreteGrid();
        GridPosition gp1 = Mockito.mock(GridPosition.class);
        Mockito.when(gp1.getX()).thenReturn(0);
        Mockito.when(gp1.getY()).thenReturn(-1);
        GridPosition gp2 = Mockito.mock(GridPosition.class);
        Mockito.when(gp2.getX()).thenReturn(0);
        Mockito.when(gp2.getY()).thenReturn(1);
        GridPosition gp3 = Mockito.mock(GridPosition.class);
        Mockito.when(gp3.getX()).thenReturn(0);
        Mockito.when(gp3.getY()).thenReturn(-2);
        GridPosition gp4 = Mockito.mock(GridPosition.class);
        Mockito.when(gp4.getX()).thenReturn(0);
        Mockito.when(gp4.getY()).thenReturn(2);

        grid.putCard(startPos, blankCard);
        grid.endTurn();
        grid.putCard(gp1, blankCard);
        grid.endTurn();
        grid.putCard(gp2, blankCard);
        grid.endTurn();
        assertFalse(grid.canPutCard(gp3));
        assertFalse(grid.canPutCard(gp4));
    }

    // checks that put and get function on the same axes
    @Test
    public void putGetTest() {
        Grid grid = new ConcreteGrid();
        GridPosition gp1 = Mockito.mock(GridPosition.class);
        Mockito.when(gp1.getX()).thenReturn(0);
        Mockito.when(gp1.getY()).thenReturn(-1);

        grid.putCard(startPos,blankCard);
        grid.endTurn();
        grid.putCard(gp1, blankCard);

        assertEquals(Optional.of(blankCard), grid.getCard(gp1));
    }

    @Test
    public void blankTest() {
        Grid grid = new ConcreteGrid();

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                GridPosition gp = Mockito.mock(GridPosition.class);
                Mockito.when(gp.getX()).thenReturn(i);
                Mockito.when(gp.getY()).thenReturn(j);

                assertEquals(Optional.empty(), grid.getCard(gp));
            }
        }
    }

    @Test
    public void activationTest() {
        Grid grid = new ConcreteGrid();
        GridPosition gp1 = Mockito.mock(GridPosition.class);
        Mockito.when(gp1.getX()).thenReturn(0);
        Mockito.when(gp1.getY()).thenReturn(-1);

        grid.putCard(startPos, blankCard);
        assertTrue(grid.canBeActivated(startPos));
        assertDoesNotThrow(()-> grid.setActivated(startPos));
        assertFalse(grid.canBeActivated(startPos));
        assertThrows(IllegalArgumentException.class, ()-> grid.setActivated(startPos));
        assertFalse(grid.canBeActivated(gp1));
        assertThrows(IllegalArgumentException.class, ()-> grid.setActivated(gp1));

        grid.endTurn();

        grid.putCard(gp1, blankCard);
        assertTrue(grid.canBeActivated(startPos));
        assertTrue(grid.canBeActivated(gp1));

        grid.endTurn();
        assertFalse(grid.canBeActivated(startPos));
        assertFalse(grid.canBeActivated(gp1));
    }

    @Test
    public void ActivationPatternTest() {
        Grid grid = new ConcreteGrid();
        ActivationPattern ap = Mockito.mock(ActivationPattern.class);

        ArrayList<AbstractMap.SimpleEntry<Integer,Integer>> pattern = new ArrayList<>();
        pattern.add(new AbstractMap.SimpleEntry<>(-1, -1));
        pattern.add(new AbstractMap.SimpleEntry<>(0, 0));
        pattern.add(new AbstractMap.SimpleEntry<>(0, -1));
        pattern.add(new AbstractMap.SimpleEntry<>(1, 1));

        Mockito.when(ap.getPattern()).thenReturn(new ArrayList<>(pattern));

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                GridPosition gp = Mockito.mock(GridPosition.class);
                Mockito.when(gp.getX()).thenReturn(i);
                Mockito.when(gp.getY()).thenReturn(j);
                grid.putCard(gp, blankCard);
                grid.endTurn();
            }
        }

        grid.setActivationPattern(ap);

        assertTrue(grid.canBeActivated(startPos));
        GridPosition gp1 = Mockito.mock(GridPosition.class);
        Mockito.when(gp1.getX()).thenReturn(2);
        Mockito.when(gp1.getY()).thenReturn(2);
        assertTrue(grid.canBeActivated(gp1));

        System.out.println(grid.state());
    }


}
