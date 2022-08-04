public class EuchreGame {
    public static CardDeck euchreDeck = new CardDeck("euchre");

    public static void main(String[] args) {
        int[] deck = euchreDeck.getDeck();
        deck = euchreDeck.shuffleDeck();
        for (int card: deck) {
            System.out.println(card);
        }

    }
}
