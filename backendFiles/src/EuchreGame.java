import java.util.*;

public class EuchreGame {
    public static CardDeck euchreDeck = new CardDeck("euchre");
    public static int userScore = 0;
    public static int opponentScore = 0;
    public static Player dealer;

    public static int getRoundWinner (String trumpSuit, Card[] playedCards) {
        int maxScore = 0;
        int maxIndex = 0;


        System.out.println("\nStart of new round:");
        for (int cardIndex = 0; cardIndex < 4; cardIndex++) {
            System.out.println(playedCards[cardIndex].getFaceValue() + "of" + playedCards[cardIndex].getSuit());
            int currentScore = 0;
            if (playedCards[cardIndex].getSuit().equals(trumpSuit)) {
                currentScore += 16;
                if (playedCards[cardIndex].getSuit().equals("Jack")) {
                    currentScore += 16;
                } else {
                    currentScore += playedCards[cardIndex].getCardValue();
                }
            } else {
                currentScore += playedCards[cardIndex].getCardValue();
            }
            if (currentScore >maxScore) {
                maxIndex = cardIndex;
                maxScore = currentScore;
            }
        }

        //dealer order is only preserved if this method returns zero, need to look into usage around this method
        //return maxIndex;
        return 0;
    }

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

        while(userScore < 10 && opponentScore < 10) {
            //choosing a new dealer

            int userRoundsWon = 0;
            int oppRoundsWon = 0;
            dealer = dealerQueue.remove(0);
            Player firstDealer = dealer;
            System.out.println("\nDealer is: " + dealer.getName());
            deck = euchreDeck.shuffleDeck();
            dealerQueue.add(dealer);
            Card[] kitty = new Card[4];
            Card topCard;
            Player trumpChooser = null;
            boolean picked = false;
            String trumpSuit = "error";
            int userRoundScore = 0;
            int oppRoundScore = 0;

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
                    trumpChooser = currentReciever;
                    picked = true;
                    trumpSuit = topCard.getSuit();
                    dealer.pickUpCard(topCard, trumpSuit);

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
                        trumpChooser = currentReciever;
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
            Player currentPlayer = dealerQueue.remove(0);
            dealerQueue.add(currentPlayer);

            //start of actually playing the game, 5 rounds for each deal
            for (int roundNumber = 0; roundNumber < 5; roundNumber++) {
                String suitLed = "empty";
                int turnIndex = 0;
                Card[] cardsPlayed = new Card[4];

                //code to run through queue and have each player play a card
                for (int playerIndex = 0; playerIndex < 4; playerIndex++) {
                    currentPlayer = dealerQueue.remove(0);
                    //System.out.println(currentPlayer.getName());
                    cardsPlayed[playerIndex] = currentPlayer.chooseCardToPlay(cardsPlayed, trumpSuit, playerIndex);
                    dealerQueue.add(currentPlayer);
                }
                int winnerIndex = getRoundWinner(trumpSuit, cardsPlayed);

                for (int playerIndex = 0; playerIndex <= winnerIndex; playerIndex++) {
                    currentPlayer = dealerQueue.remove(playerIndex);
                    dealerQueue.add(currentPlayer);
                }
                if (currentPlayer.getName().equals("user") || currentPlayer.getName().equals("userPartner")) {
                    userRoundScore++;
                    System.out.println("user point");
                }
                if (currentPlayer.getName().equals("opponent1") || currentPlayer.getName().equals("opponent2")) {
                    oppRoundScore++;
                    System.out.println("opp point");
                }
            }
            //before changing who leads a round, this works to get the right dealer
            while (!currentPlayer.getName().equals(firstDealer.getName())) {
                currentPlayer = dealerQueue.remove(0);
                dealerQueue.add(currentPlayer);
            }

            //still need to track user and opponent score, let user make interactive choices, and move to whoever
            // won the hand so that they can go first on the next round (maybe just add them at front of arraylist once
            // you have looped through and found them)
            if (userRoundScore > oppRoundScore) {
                if (trumpChooser.getName().equals("user") || trumpChooser.getName().equals("userPartner")) {
                    userScore++;
                } else {
                    userScore+=2;
                }
            } else {
                if (trumpChooser.getName().equals("opponent1") || trumpChooser.getName().equals("opponent2")) {
                    opponentScore++;
                } else {
                    opponentScore+=2;
                }
            }

        }
        if (userScore >= 10) {
            System.out.println("\nCongrats you have won this game of Euchre!");
        } else if (opponentScore >= 10) {
            System.out.println("\nYour opponent's won, better luck next time");
        }
        System.out.println("\nFinal Score: \nUser Team " + userScore + "\nOpponentScore " + opponentScore);
    }
}
