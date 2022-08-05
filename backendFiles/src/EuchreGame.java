import java.util.*;

public class EuchreGame {
    public static CardDeck euchreDeck = new CardDeck("euchre");
    public static int userScore = 0;
    public static int opponentScore = 0;
    public static Player dealer;

    public static void main(String[] args) {
        Card[] deck = euchreDeck.getDeck();
        Player currentUser = new Player("user", "userPartner");
        Player userPartner = new Player("userPartner", "user");
        Player opponent1 = new Player("opponent1", "opponent2");
        Player opponent2 = new Player("opponent2", "opponent1");
        Queue<Player> dealerQueue = new LinkedList<>();

        dealerQueue.add(currentUser);
        dealerQueue.add(opponent1);
        dealerQueue.add(userPartner);
        dealerQueue.add(opponent2);
        //test to check card values
        /*
        for (Card card: deck) {
            System.out.print(card.getFaceValue() + " ");
            System.out.println(card.getSuit());
        }


        System.out.println();

        currentUser.addToHand(deck[0]);
        currentUser.addToHand(deck[1]);
        */

        while(userScore != 10 || opponentScore != 10) {
            //choosing a new dealer
            dealer = dealerQueue.remove();
            deck = euchreDeck.shuffleDeck();
            dealerQueue.add(dealer);
            Card[] kitty = new Card[4];

            //section to deal cards
            for (int currentIndex = 0; currentIndex < 20; currentIndex++) {
                Player currentReciever = dealerQueue.remove();
                currentReciever.addToHand(deck[currentIndex]);
                dealerQueue.add(currentReciever);
            }
            for (int kittyIndex = 0; kittyIndex < 4; kittyIndex++) {
                kitty[kittyIndex] = deck[kittyIndex + 20];
            }


            //section to print a user's hand
            System.out.println("\nUser's hand:");
            for (Card currentCard : currentUser.getHand()) {
                System.out.println(currentCard.getFaceValue() + " " + currentCard.getSuit());
            }
            System.out.println("\nopp1's hand:");
            for (Card currentCard : opponent1.getHand()) {
                System.out.println(currentCard.getFaceValue() + " " + currentCard.getSuit());
            }

            System.out.println("\npartner's hand:");
            for (Card currentCard : userPartner.getHand()) {
                System.out.println(currentCard.getFaceValue() + " " + currentCard.getSuit());
            }
            System.out.println("\nopp2's hand:");
            for (Card currentCard : opponent2.getHand()) {
                System.out.println(currentCard.getFaceValue() + " " + currentCard.getSuit());
            }
            System.out.println("\nTop card is: " + kitty[0].getFaceValue() + " of " + kitty[0].getSuit());

            //temporary break while score is not working
            break;
        }
        if (userScore >= 10) {
            System.out.println("Congrats you have won this game of Euchre!");
        } else if (opponentScore >= 10) {
            System.out.println("Your opponent's won, better luck next time");
        }
        System.out.println("Final Score: \nUser Team " + userScore + "\nOpponentScore " + opponentScore);
    }
}
