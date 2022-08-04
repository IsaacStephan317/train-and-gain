import java.util.*;

public class CardDeck {
    private int[] deck;

    public CardDeck() {
        this.deck = new int[52];
        int deckIndex = 0;

        for (int currValue = 2; currValue <= 14; currValue++) {
            for (int numbCardsAdded = 1; numbCardsAdded <= 4; numbCardsAdded++) {
                deck[deckIndex] = currValue;
                deckIndex++;
            }
        }
    }

    public CardDeck(String deckType) {
        if (deckType.equals("euchre")) {
            this.deck = new int[24];
            int deckIndex = 0;

            for (int currValue = 9; currValue <= 14; currValue++) {
                for (int numbCardsAdded = 1; numbCardsAdded <= 4; numbCardsAdded++) {
                    deck[deckIndex] = currValue;
                    deckIndex++;
                }
            }
        }
    }

    public int[] shuffleDeck() {
        Random rand = new Random();

        for (int currIndex = 0; currIndex < this.deck.length; currIndex++) {
            int swap = rand.nextInt(this.deck.length);
            int temp = this.deck[swap];
            this.deck[swap] = this.deck[currIndex];
            this.deck[currIndex] = temp;
        }
        return this.deck;
    }

    public int[] getDeck() {
        return deck;
    }
}
