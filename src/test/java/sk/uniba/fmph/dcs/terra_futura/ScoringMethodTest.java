package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;
import static sk.uniba.fmph.dcs.terra_futura.Resource.CAR;
import static sk.uniba.fmph.dcs.terra_futura.Resource.GREEN;

class ResourcesCardFake {
    public static Map<Resource, Integer> actuallyGetResources() {
        Map<Resource, Integer> result = new HashMap<>();
        for (Resource resource : Resource.values()) {
            result.put(resource, 1);
        }
        return result;
    }
}


public class ScoringMethodTest {

    private ScoringMethod scoringMethod1;
    private ScoringMethod scoringMethod2;

    @Before
    public void setUp() {
        Card pollutedCardFake = Mockito.mock(Card.class);
        Card resourceCardFake = Mockito.mock(Card.class);
        Grid gridFake1 = Mockito.mock(Grid.class);
        Grid gridFake2 = Mockito.mock(Grid.class);

        Mockito.when(pollutedCardFake.isPolluted()).thenReturn(true);
        Mockito.when(resourceCardFake.isPolluted()).thenReturn(false);
        Mockito.when(pollutedCardFake.actuallyGetResources()).thenThrow(new RuntimeException());
        Mockito.when(resourceCardFake.actuallyGetResources()).thenReturn(ResourcesCardFake.actuallyGetResources());
        Mockito.when(gridFake1.getCard(Mockito.any())).thenReturn(Optional.of(pollutedCardFake));
        Mockito.when(gridFake2.getCard(Mockito.any())).thenReturn(Optional.of(resourceCardFake));

        List<Resource> resources = new ArrayList<>();
        resources.add(GREEN);
        resources.add(GREEN);
        resources.add(CAR);
        scoringMethod1 = new ScoringMethod(resources, 5, gridFake1);
        scoringMethod2 = new ScoringMethod(resources, 5, gridFake2);
    }

    @Test
    public void ScoringTest1() {
        System.out.println("State before calculation:");
        System.out.println(scoringMethod1.state());
        assertFalse(scoringMethod1.getCalculatedTotal().isPresent());

        System.out.println("State after calculation:");
        scoringMethod1.selectThisMethodAndCalculate();
        System.out.println(scoringMethod1.state());
        assertTrue(scoringMethod1.getCalculatedTotal().isPresent());
        assertEquals(Optional.of(-25), scoringMethod1.getCalculatedTotal());
    }

    @Test
    public void ScoringTest2() {
        System.out.println("State before calculation:");
        System.out.println(scoringMethod2.state());
        assertFalse(scoringMethod2.getCalculatedTotal().isPresent());

        System.out.println("State after calculation:");
        scoringMethod2.selectThisMethodAndCalculate();
        System.out.println(scoringMethod2.state());
        assertTrue(scoringMethod2.getCalculatedTotal().isPresent());
        assertEquals(Optional.of(510), scoringMethod2.getCalculatedTotal());
    }

}
