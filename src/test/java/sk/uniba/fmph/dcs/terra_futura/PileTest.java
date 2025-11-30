package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class PileTest {
    private List<Card> cardList;
    private Random random1;
    private Random random2;

    @Before
    public void setUp() {
        random1 = Mockito.mock(Random.class);
        Mockito.when(random1.nextInt()).thenReturn(0);

        random2 = Mockito.mock(Random.class);
        Mockito.when(random2.nextInt()).thenReturn(10,2,3,0);

        cardList = new ArrayList<>(); // 10 x card1, 5 x card2, i x card3

        Card card1 = Mockito.mock(Card.class);
        Mockito.when(card1.state()).thenReturn("1");
        Card card2 = Mockito.mock(Card.class);
        Mockito.when(card2.state()).thenReturn("2");
        Card card3 = Mockito.mock(Card.class);
        Mockito.when(card3.state()).thenReturn("3");

        for (int i = 0; i < 10; i++) {
            cardList.add(card1);
        }

        for (int i = 0; i < 5; i++) {
            cardList.add(card2);
        }

        cardList.add(card3);
    }


    @Test
    public void removeTest() {
        Pile concretePile = new ConcretePile(cardList, random2);

        // tests if repeated removing of last card keeps pile content intact
        for (int i = 0; i < 100; i++) {
            concretePile.removeLastCard();
        }

        // actually removes all cards in deck
        for (int i = 0; i < cardList.size(); i++) {
            concretePile.takeCard(4);
        }

        // asserts that no cards remain
        for (int i = 0; i < 5; i++) {
            assertEquals(Optional.empty(), concretePile.getCard(i));
        }
    }

    @Test
    public void getTakeTest() {
        List<Card> obtainedCards = new ArrayList<>();

        Pile concretePile = new ConcretePile(cardList, random1);

        for (int i = 0; i < cardList.size(); i++) {
            obtainedCards.add(concretePile.getCard(4).get()); // should always be present
            concretePile.takeCard(4);
        }

        System.out.println("Initial cards: ");
        cardList.forEach((card -> System.out.print(card.state())));
        System.out.println();
        System.out.println("Cards obtained from deck: ");
        obtainedCards.forEach((card -> System.out.print(card.state())));

        assertEquals(cardList, obtainedCards);
        // for any other randomness, this fails, but it should succeed for all 0
    }

    @Test
    public void getTakeTest2() {
        List<Integer> obtainedCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            obtainedCards.add(0);
        }

        Pile concretePile = new ConcretePile(cardList, random2);

        for (int i = 0; i < 100; i++) {
            concretePile.removeLastCard();
        }

        for (int i = 0; i < cardList.size(); i++) {
            switch (concretePile.getCard(4).get().state()) {
                case "1":
                    obtainedCards.set(0, obtainedCards.get(0) + 1);
                    break;
                case "2":
                    obtainedCards.set(1, obtainedCards.get(1) + 1);
                    break;
                case "3":
                    obtainedCards.set(2, obtainedCards.get(2) + 1);
                    break;

            }
            concretePile.takeCard(4);
        }

        List<Integer> expected = new ArrayList<>();
        expected.add(10);
        expected.add(5);
        expected.add(1);
        assertEquals(expected, obtainedCards);

        for (int i = 0; i < 5; i++) {
            assertEquals(Optional.empty(), concretePile.getCard(i));
        }
    }
}


