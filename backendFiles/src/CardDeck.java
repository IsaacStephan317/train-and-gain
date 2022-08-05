import java.util.*;

public class CardDeck {
    private Card[] deck;

    public CardDeck() {
        this.deck = new Card[52];
        int deckIndex = 0;

        for (int currValue = 2; currValue <= 14; currValue++) {
            for (int numbCardsAdded = 1; numbCardsAdded <= 4; numbCardsAdded++) {
                deck[deckIndex] = new Card(currValue, numbCardsAdded);
                deckIndex++;
            }
        }
    }

    public CardDeck(String deckType) {
        if (deckType.equals("euchre")) {
            this.deck = new Card[24];
            int deckIndex = 0;

            for (int currValue = 9; currValue <= 14; currValue++) {
                for (int numbCardsAdded = 1; numbCardsAdded <= 4; numbCardsAdded++) {
                    deck[deckIndex] = new Card(currValue, numbCardsAdded);
                    deckIndex++;
                }
            }
        }
    }

    public Card[] shuffleDeck() {
        Random rand = new Random();

        for (int currIndex = 0; currIndex < this.deck.length; currIndex++) {
            int swap = rand.nextInt(this.deck.length);
            Card temp = this.deck[swap];
            this.deck[swap] = this.deck[currIndex];
            this.deck[currIndex] = temp;
        }
        return this.deck;
    }

    public Card[] getDeck() {
        return deck;
    }
}
