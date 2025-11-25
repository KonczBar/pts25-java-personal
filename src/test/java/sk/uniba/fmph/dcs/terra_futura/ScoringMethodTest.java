package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static sk.uniba.fmph.dcs.terra_futura.Resource.CAR;
import static sk.uniba.fmph.dcs.terra_futura.Resource.GREEN;

class GridFake implements Grid {

    @Override
    public Optional<Card> getCard(GridPosition coordinate) {
        if (coordinate.getX() < 0 || coordinate.getY() < 0) {
            return Optional.empty();
        }

        if (coordinate.getX() == 0) {
            return Optional.of(new PollutedCardFake());
        }

        return Optional.of(new ResourcesCardFake());
    }
}

class PollutedCardFake implements Card {

    @Override
    public boolean isPolluted() {
        return true;
    }

    @Override
    public Map<Resource, Integer> actuallyGetResources() {
        throw new RuntimeException();
    }
}

class ResourcesCardFake implements Card {

    @Override
    public boolean isPolluted() {
        return false;
    }

    @Override
    public Map<Resource, Integer> actuallyGetResources() {
        Map<Resource, Integer> result = new HashMap<>();
        for (Resource resource : Resource.values()) {
            result.put(resource, 1);
        }
        return result;
    }
}

public class ScoringMethodTest {

    private ScoringMethod scoringMethod;

    @Before
    public void setUp() {
        List<Resource> resources = new ArrayList<>();
        resources.add(GREEN);
        resources.add(GREEN);
        resources.add(CAR);
        scoringMethod = new ScoringMethod(resources, 5, new GridFake());
    }

    @Test
    public void ScoringTest() {
        System.out.println("State before calculation:");
        System.out.println(scoringMethod.state());
        assertFalse(scoringMethod.getCalculatedTotal().isPresent());

        System.out.println("State after calculation:");
        scoringMethod.selectThisMethodAndCalculate();
        System.out.println(scoringMethod.state());
        assertTrue(scoringMethod.getCalculatedTotal().isPresent());
        assertEquals(Optional.of(120), scoringMethod.getCalculatedTotal());
    }

}
