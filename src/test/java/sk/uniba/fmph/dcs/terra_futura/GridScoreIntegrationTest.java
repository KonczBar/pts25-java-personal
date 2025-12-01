package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static sk.uniba.fmph.dcs.terra_futura.Resource.CAR;
import static sk.uniba.fmph.dcs.terra_futura.Resource.GREEN;

public class GridScoreIntegrationTest {
    private ScoringMethod scoringMethod1;
    private ScoringMethod scoringMethod2;

    @Before
    public void setUp() {
        Card pollutedCardFake = Mockito.mock(Card.class);
        Card resourceCardFake = Mockito.mock(Card.class);

        Grid grid1 = new ConcreteGrid();
        Grid grid2 = new ConcreteGrid();

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                GridPosition gp = Mockito.mock(GridPosition.class);
                Mockito.when(gp.getX()).thenReturn(i);
                Mockito.when(gp.getY()).thenReturn(j);
                grid1.putCard(gp, pollutedCardFake);
                grid2.putCard(gp, resourceCardFake);
                grid1.endTurn();
                grid2.endTurn();
            }
        }

        Mockito.when(pollutedCardFake.isPolluted()).thenReturn(true);
        Mockito.when(resourceCardFake.isPolluted()).thenReturn(false);
        Mockito.when(pollutedCardFake.actuallyGetResources()).thenThrow(new RuntimeException());
        Mockito.when(resourceCardFake.actuallyGetResources()).thenReturn(ResourcesCardFake.actuallyGetResources());

        List<Resource> resources = new ArrayList<>();
        resources.add(GREEN);
        resources.add(GREEN);
        resources.add(CAR);
        scoringMethod1 = new ScoringMethod(resources, 5, grid1);
        scoringMethod2 = new ScoringMethod(resources, 5, grid2);
    }

    @Test
    public void GridScore1() {
        System.out.println("State before calculation:");
        System.out.println(scoringMethod1.state());
        assertFalse(scoringMethod1.getCalculatedTotal().isPresent());

        System.out.println("State after calculation:");
        scoringMethod1.selectThisMethodAndCalculate();
        System.out.println(scoringMethod1.state());
        assertTrue(scoringMethod1.getCalculatedTotal().isPresent());
        assertEquals(Optional.of(-9), scoringMethod1.getCalculatedTotal());
    }

    @Test
    public void GridScore2() {
        System.out.println("State before calculation:");
        System.out.println(scoringMethod2.state());
        assertFalse(scoringMethod2.getCalculatedTotal().isPresent());

        System.out.println("State after calculation:");
        scoringMethod2.selectThisMethodAndCalculate();
        System.out.println(scoringMethod2.state());
        assertTrue(scoringMethod2.getCalculatedTotal().isPresent());
        assertEquals(Optional.of(182), scoringMethod2.getCalculatedTotal());
    }
}
