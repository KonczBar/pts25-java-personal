package sk.uniba.fmph.dcs.terra_futura;

// in Java this can't be made an enum
public class CardSource {
    private Deck deck;
    private int index;

    public CardSource(Deck deck, int cardIndex) {
        this.deck = deck;
        this.index = cardIndex;
    }
}
