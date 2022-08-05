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
            Card topCard;
            boolean picked = false;
            String trumpSuit = "error";

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

            topCard = kitty[0];
            for (int currentIndex = 0; currentIndex < 4; currentIndex++) {
                Player currentReciever = dealerQueue.remove();
                String decision = currentReciever.pickOrPass(topCard, dealer);
                dealerQueue.add(currentReciever);
                if (decision.equals("Pick")) {
                    //code for dealer to swap if computer or have player choose which card to drop
                    System.out.println("pick up: " + currentReciever.getName());
                    picked = true;
                    trumpSuit = topCard.getSuit();
                }
            }

            if (picked == false) {
                String pickSuit = null;
                for (int currentIndex = 0; currentIndex < 4; currentIndex++) {
                    Player currentReciever = dealerQueue.remove();
                    pickSuit = currentReciever.suitOrPass(dealer);
                    dealerQueue.add(currentReciever);
                    if (pickSuit != null && !pickSuit.equals("Pass")) {
                        System.out.println("chose trump: " + currentReciever.getName());
                        trumpSuit = pickSuit;

                        break;
                    }
                }
            }
            System.out.println("trump suit is "+ trumpSuit);
            //temporary break while score is not working
            break;
        }
        if (userScore >= 10) {
            System.out.println("\nCongrats you have won this game of Euchre!");
        } else if (opponentScore >= 10) {
            System.out.println("\nYour opponent's won, better luck next time");
        }
        System.out.println("\nFinal Score: \nUser Team " + userScore + "\nOpponentScore " + opponentScore);
    }
}
