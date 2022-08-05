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
        //may want to change this from a queue to an arraylist
        ArrayList<Player> dealerQueue = new ArrayList<>();

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

        while(userScore < 10 && opponentScore < 10) {
            //choosing a new dealer
            int roundsWon = 0;
            dealer = dealerQueue.remove(0);
            System.out.println("\nDealer is: " + dealer.getName());
            deck = euchreDeck.shuffleDeck();
            dealerQueue.add(dealer);
            Card[] kitty = new Card[4];
            Card topCard;
            boolean picked = false;
            String trumpSuit = "error";

            //section to deal cards
            for (int currentIndex = 0; currentIndex < 20; currentIndex++) {
                Player currentReciever = dealerQueue.remove(0);
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
                Player currentReciever = dealerQueue.remove(0);
                String decision = currentReciever.pickOrPass(topCard, dealer);
                dealerQueue.add(currentReciever);
                if (decision.equals("Pick")) {
                    //code for dealer to swap if computer or have player choose which card to drop
                    System.out.println("pick up: " + currentReciever.getName());
                    picked = true;
                    trumpSuit = topCard.getSuit();
                    for (int index = currentIndex+1; index < 4; index++) {
                        currentReciever = dealerQueue.remove(0);
                        dealerQueue.add(currentReciever);
                    }
                    break;
                }
            }

            if (picked == false) {
                String pickSuit = null;
                for (int currentIndex = 0; currentIndex < 4; currentIndex++) {
                    Player currentReciever = dealerQueue.remove(0);
                    pickSuit = currentReciever.suitOrPass(dealer);
                    dealerQueue.add(currentReciever);
                    if (pickSuit != null && !pickSuit.equals("Pass")) {
                        System.out.println("chose trump: " + currentReciever.getName());
                        trumpSuit = pickSuit;
                        for (int index = currentIndex+1; index < 4; index++) {
                            currentReciever = dealerQueue.remove(0);
                            dealerQueue.add(currentReciever);
                        }
                        break;
                    }
                }
            }
            System.out.println("trump suit is "+ trumpSuit);

            //start of actually playing the game, 5 rounds for each deal
            for (int roundNumber = 0; roundNumber < 5; roundNumber++) {


                //ignore
                //temporary code to regulate cards in a user's hand
                currentUser.removeFromHand(0);
                opponent1.removeFromHand(0);
                userPartner.removeFromHand(0);
                opponent2.removeFromHand(0);
            }
            //temporary break while score is not working

            userScore+=2;
        }
        if (userScore >= 10) {
            System.out.println("\nCongrats you have won this game of Euchre!");
        } else if (opponentScore >= 10) {
            System.out.println("\nYour opponent's won, better luck next time");
        }
        System.out.println("\nFinal Score: \nUser Team " + userScore + "\nOpponentScore " + opponentScore);
    }
}
