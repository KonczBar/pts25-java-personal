package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Test;
import org.mockito.Mockito;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SelectRewardTest {
    @Test
    public void notSetTest() {
        SelectReward s = new SelectReward();
        assertFalse(s.canSelectReward(Resource.CAR));
        assertThrows(IllegalArgumentException.class, ()->s.selectReward(Resource.CAR));
        System.out.println(s.state());
    }

    @Test
    public void generalTest() {
        SelectReward s = new SelectReward();
        List<Resource> availableResources = new ArrayList<>();
        availableResources.add(Resource.CAR);
        availableResources.add(Resource.GREEN);
        availableResources.add(Resource.CAR);
        availableResources.add(Resource.POLLUTION);

        Card goodCard = Mockito.mock(Card.class);
        Mockito.when(goodCard.canPutResources(Mockito.any())).thenReturn(true);
        Card badCard = Mockito.mock(Card.class);
        Mockito.when(badCard.canPutResources(Mockito.any())).thenReturn(false);

        s.setReward(1, goodCard, availableResources);

        System.out.println(s.state());

        assertTrue(s.canSelectReward(Resource.GREEN));
        assertFalse(s.canSelectReward(Resource.BULB));
        assertDoesNotThrow(()->s.selectReward(Resource.GREEN));
        assertFalse(s.canSelectReward(Resource.GREEN));

        System.out.println(s.state());

        s.setReward(2, badCard, availableResources);
        assertFalse(s.canSelectReward(Resource.GREEN));

        System.out.println(s.state());
    }
}
