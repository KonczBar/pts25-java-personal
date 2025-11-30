package sk.uniba.fmph.dcs.terra_futura;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ConcretePile implements Pile {
    private final List<Card> cardDeck;
    private final List<Optional<Card>> availableCards; // numbered 1 - newest, 4 - oldest !!!
    private List<Card> discardPile;
    private final Random random;

    public ConcretePile(List<Card> cardDeck) {
        this.cardDeck = new ArrayList<>(cardDeck); // cards within the deck, not shuffled
        this.availableCards = new ArrayList<>();
        this.random = new Random();
        this.discardPile = new ArrayList<>();
        initializeAvailable();
    }

    // for testing
    public ConcretePile(List<Card> cardDeck, Random random) {
        this.cardDeck = new ArrayList<>(cardDeck);
        this.availableCards = new ArrayList<>();
        this.random = random;
        this.discardPile = new ArrayList<>();
        initializeAvailable();
    }

    // this application of a bound results in slightly uneven randomness,
    // but it facilitates good testing
    private Optional<Card> getRandomCard() {
        if (cardDeck.isEmpty()) {
            shuffleDiscardIntoDeck();
        }

        if (cardDeck.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(cardDeck.remove(random.nextInt() % cardDeck.size()));
    }

    private void shuffleDiscardIntoDeck() {
        cardDeck.addAll(discardPile);
        discardPile = new ArrayList<>();
    }

    private void initializeAvailable() {
        if (cardDeck.size() < 5) {
            throw new IllegalArgumentException("Invalid size of cardDeck: " + availableCards.size() + " is too few.");
        }
        for (int i = 0; i < 5; i++) {
            availableCards.addFirst(getRandomCard());
        }
    }

    @Override
    public Optional<Card> getCard(int index) {
        if (index < 0 || index > 4) {
            throw new IndexOutOfBoundsException("Attempted to access invalid Pile index:" + index);
        }

        return availableCards.get(index);
    }

    @Override
    public void takeCard(int index) {
        if (index < 0 || index > 4) {
            throw new IndexOutOfBoundsException("Attempted to access invalid Pile index:" + index);
        }

        availableCards.remove(index);
        availableCards.addFirst(getRandomCard());
    }

    @Override
    public void removeLastCard() {
        if (availableCards.get(4).isPresent()) {
            discardPile.add(availableCards.remove(4).get());
        } else {
            availableCards.remove(4);
        }

        availableCards.addFirst(getRandomCard());
    }

    @Override
    public String state() {
        return "Available: " + availableCards.toString() + " Remaining: " + cardDeck.size();
    }
}
