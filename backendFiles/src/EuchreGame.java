public class EuchreGame {
    public static CardDeck euchreDeck = new CardDeck("euchre");

    public static void main(String[] args) {
        int[] deck = euchreDeck.getDeck();
        UserPlayer currentUser = new UserPlayer();

        deck = euchreDeck.shuffleDeck();
        for (int card: deck) {
            System.out.println(card);
        }


    }
}
