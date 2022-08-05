public class EuchreGame {
    public static CardDeck euchreDeck = new CardDeck("euchre");

    public static void main(String[] args) {
        Card[] deck = euchreDeck.getDeck();
        UserPlayer currentUser = new UserPlayer();

        deck = euchreDeck.shuffleDeck();
        for (Card card: deck) {
            System.out.println(card.getFaceValue());
        }


    }
}
