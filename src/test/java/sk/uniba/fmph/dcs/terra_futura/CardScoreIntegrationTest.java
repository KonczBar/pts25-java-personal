package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static sk.uniba.fmph.dcs.terra_futura.Resource.CAR;
import static sk.uniba.fmph.dcs.terra_futura.Resource.GREEN;

public class CardScoreIntegrationTest {

    // polluted cards
    @Test
    public void CardScore1() {
        Map<Resource, Integer> result = new HashMap<>();
        for (Resource resource : Resource.values()) {
            result.put(resource, 0);
        }

        result.put(Resource.POLLUTION, 10);

        Card card1 = new Card(result, 0);
        Grid gridFake = Mockito.mock(Grid.class);
        Mockito.when(gridFake.getCard(Mockito.any())).thenReturn(Optional.of(card1));

        List<Resource> resources = new ArrayList<>();
        resources.add(GREEN);
        resources.add(GREEN);
        resources.add(CAR);

        ScoringMethod scoringMethod1 = new ScoringMethod(resources, 5, gridFake);

        System.out.println("State before calculation:");
        System.out.println(scoringMethod1.state());
        assertFalse(scoringMethod1.getCalculatedTotal().isPresent());

        System.out.println("State after calculation:");
        scoringMethod1.selectThisMethodAndCalculate();
        System.out.println(scoringMethod1.state());
        assertTrue(scoringMethod1.getCalculatedTotal().isPresent());
        assertEquals(Optional.of(-25), scoringMethod1.getCalculatedTotal());
    }

    // standard cards

    @Test
    public void CardScore2() {
        Map<Resource, Integer> result = new HashMap<>();
        for (Resource resource : Resource.values()) {
            result.put(resource, 1);
        }

        Card card1 = new Card(result, 100);
        Grid gridFake = Mockito.mock(Grid.class);
        Mockito.when(gridFake.getCard(Mockito.any())).thenReturn(Optional.of(card1));

        List<Resource> resources = new ArrayList<>();
        resources.add(GREEN);
        resources.add(GREEN);
        resources.add(CAR);

        ScoringMethod scoringMethod1 = new ScoringMethod(resources, 5, gridFake);

        System.out.println("State before calculation:");
        System.out.println(scoringMethod1.state());
        assertFalse(scoringMethod1.getCalculatedTotal().isPresent());

        System.out.println("State after calculation:");
        scoringMethod1.selectThisMethodAndCalculate();
        System.out.println(scoringMethod1.state());
        assertTrue(scoringMethod1.getCalculatedTotal().isPresent());
        assertEquals(Optional.of(510), scoringMethod1.getCalculatedTotal());
    }
}
