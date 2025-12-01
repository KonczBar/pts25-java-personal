package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class PileCardIntegrationTest {
    private List<Card> cardList;
    private Random random1;
    private Random random2;

    @Before
    public void setUp() {
        random1 = Mockito.mock(Random.class);
        Mockito.when(random1.nextInt()).thenReturn(0);

        random2 = Mockito.mock(Random.class);
        Mockito.when(random2.nextInt()).thenReturn(10, 2, 3, 0);

        cardList = new ArrayList<>(); // 10 x card1, 5 x card2, i x card3

        Card cleanCard = new Card(0);
        Card pollutedCard = new Card(0);
        Map<Resource, Integer> pollution = new HashMap<>();
        pollution.put(Resource.POLLUTION, 1);
        pollutedCard.putResources(pollution);

        for (int i = 0; i < 5; i++) {
            cardList.add(cleanCard);
        }

        cardList.add(pollutedCard);
    }

    @Test
    public void getTakeTest() {
        int polluted = 0;
        int clean = 0;

        Pile concretePile = new ConcretePile(cardList, random2);

        for (int i = 0; i < 100; i++) {
            concretePile.removeLastCard();
        }

        for (int i = 0; i < cardList.size(); i++) {
            if (concretePile.getCard(4).get().isPolluted()) {
                polluted++;
            } else {
                clean++;
            }
            concretePile.takeCard(4);
        }

        assertEquals(5, clean);
        assertEquals(1, polluted);

        for (int i = 0; i < 5; i++) {
            assertEquals(Optional.empty(), concretePile.getCard(i));
        }
    }
}
