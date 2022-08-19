import java.util.*;

import static java.lang.Thread.sleep;

/**
 *
 * A personal project created by Isaac Stephan that hopes to give users
 * a tool to learn the game of Euchre, practice, or just kill some
 * down time
 *
 * @author , Isaac Stephan
 *
 */

public class EuchreGame {
    //initialize static variables
    public static CardDeck euchreDeck = new CardDeck("euchre");
    public static int userScore = 0;
    public static int opponentScore = 0;
    public static Player dealer;

    /**
     * check to see if a given Jack is a trump suit pair or not (Check for the left Bower)
     *
     * @param  trumpSuit  String representing the current trump suit
     * @param  possibleJack card object to check and see if it is a trump card
     * @return boolean specifying if the given jack is in fact a trump card
     *
     */
    private static boolean getJackPair(String trumpSuit, Card possibleJack) {
        if (trumpSuit.equalsIgnoreCase("Clubs") && possibleJack.getSuit().equalsIgnoreCase("Spades")) {
            return true;
        } else if (trumpSuit.equalsIgnoreCase("Spades") && possibleJack.getSuit().equalsIgnoreCase("Clubs")) {
            return true;
        } else if (trumpSuit.equalsIgnoreCase("Hearts") && possibleJack.getSuit().equalsIgnoreCase("Diamonds")) {
            return true;
        } else if (trumpSuit.equalsIgnoreCase("Diamonds") && possibleJack.getSuit().equalsIgnoreCase("Hearts")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * checks to make sure that a user has followed suit with the first card played
     *
     * @param  leadSuit  String representing the suit of the lead card played
     * @param  cardPlayed Card object representing the user's card played
     * @param  trumpSuit  String representing the current trump suit
     *
     * @return boolean specifying if the user followed suit
     *
     */
    private static boolean followSuitCheck (String leadSuit, Card cardPlayed, String trumpSuit) {
        if (getCardSuit(cardPlayed, trumpSuit).equalsIgnoreCase(leadSuit)) {
            return true;
        }
        return false;
    }

    /**
     * gets the card suit if a Jack is the left bower and a trump card
     *
     * @param  firstCard  Card object representing the given card played
     * @param  trumpSuit  String representing the current trump suit
     *
     * @return String of the given card
     *
     */
    private static String getCardSuit(Card firstCard, String trumpSuit) {
        if (firstCard.getFaceValue().equalsIgnoreCase("Jack") && getJackPair(trumpSuit, firstCard)) {
            return trumpSuit;
        } else {
            return firstCard.getSuit();
        }
    }

    /**
     * gets the card suit if a Jack is the left bower and a trump card
     *
     * @param  trumpSuit  String representing the current trump suit
     * @param  playedCards  Card array representing all the cards played in a given trick
     *
     * @return integer that indicates what index the winning player of the trick is at
     *
     */
    private static int getTrickWinner (String trumpSuit, Card[] playedCards) {
        int maxScore = 0;
        int maxIndex = 0;
        String leadSuit = getCardSuit(playedCards[0], trumpSuit);

        for (int cardIndex = 0; cardIndex < 4; cardIndex++) {
            //System.out.println(playedCards[cardIndex].getFaceValue() + "of" + playedCards[cardIndex].getSuit());
            int currentScore = 0;
            if (playedCards[cardIndex].getSuit().equalsIgnoreCase(leadSuit)) {
                currentScore += 16;
            }
            if (playedCards[cardIndex].getSuit().equalsIgnoreCase(trumpSuit)) {
                currentScore += 32;
                if (playedCards[cardIndex].getFaceValue().equalsIgnoreCase("Jack")) {
                    currentScore += 16;
                } else {
                    currentScore += playedCards[cardIndex].getCardValue();
                }
            } else if (playedCards[cardIndex].getFaceValue().equalsIgnoreCase("Jack") && getJackPair(trumpSuit, playedCards[cardIndex])) { //check to see if card is a jack of the same color
                currentScore += 47; //technically a trump (+32) and a jack pair (+15)
            } else {
                currentScore += playedCards[cardIndex].getCardValue();
            }
            if (currentScore >maxScore) {
                maxIndex = cardIndex;
                maxScore = currentScore;
            }
        }

        return maxIndex;
    }

    public static void main(String[] args) throws InterruptedException {
        Card[] deck = euchreDeck.getDeck();
        Player currentUser = new Player("user", "userPartner");
        Player userPartner = new Player("userPartner", "user");
        Player opponent1 = new Player("opponent1", "opponent2");
        Player opponent2 = new Player("opponent2", "opponent1");

        ArrayList<Player> dealerQueue = new ArrayList<>();

        //add user to the dealerQueue
        dealerQueue.add(currentUser);
        dealerQueue.add(opponent1);
        dealerQueue.add(userPartner);
        dealerQueue.add(opponent2);

        Scanner userScanner = new Scanner(System.in);  // User Scanner
        System.out.println("\nWelcome to Train and Gain, a euchre training software!");
        System.out.println("Let's start the game");

        //while loop that runs while a user does not have a score
        while(userScore < 10 && opponentScore < 10) {
            //begin to choose a new dealer and initialize variables
            Card[] kitty = new Card[4];
            Card topCard;
            Player trumpChooser = null;
            boolean picked = false;
            String trumpSuit = "error";
            int userTrickScore = 0;
            int oppTrickScore = 0;
            dealer = dealerQueue.remove(0);
            Player firstDealer = dealer;
            System.out.println("\nDealer is: " + dealer.getName());
            deck = euchreDeck.shuffleDeck();
            dealerQueue.add(dealer);

            //section to deal cards to users
            for (int currentIndex = 0; currentIndex < 20; currentIndex++) {
                Player currentReciever = dealerQueue.remove(0);
                currentReciever.addToHand(deck[currentIndex]);
                dealerQueue.add(currentReciever);
            }
            for (int kittyIndex = 0; kittyIndex < 4; kittyIndex++) {
                kitty[kittyIndex] = deck[kittyIndex + 20];
            }


            //section to print the user's hand
            System.out.println("\nUser's hand:");
            for (Card currentCard : currentUser.getHand()) {
                System.out.println(currentCard.getFaceValue() + " " + currentCard.getSuit());
            }

            //begin section to choose trump suit
            System.out.println("\nTop card is: " + kitty[0].getFaceValue() + " of " + kitty[0].getSuit());
            String pickSuitChoice;
            topCard = kitty[0];

            //section to have users pick up the card if they would like
            for (int currentIndex = 0; currentIndex < 4; currentIndex++) {
                Player currentReciever = dealerQueue.remove(0);
                dealerQueue.add(currentReciever);

                if (currentReciever.getName().equalsIgnoreCase("user")) { //section to have the user possibly pick up top card
                    System.out.println("'Pick up' or 'pass'");
                    pickSuitChoice = userScanner.nextLine();
                    if (pickSuitChoice.equalsIgnoreCase("Pick") || pickSuitChoice.equalsIgnoreCase("Pick up")) {
                        //code for dealer to swap if computer or have player choose which card to drop
                        System.out.println("Pick up: " + currentReciever.getName() + "\n");
                        trumpChooser = currentReciever;
                        picked = true;
                        trumpSuit = topCard.getSuit();
                        dealer.pickUpCard(topCard, trumpSuit);

                        while (!currentReciever.getName().equalsIgnoreCase(firstDealer.getName())) {
                            currentReciever = dealerQueue.remove(0);
                            dealerQueue.add(currentReciever);
                        }
                        break;
                    } else {
                        while (!(pickSuitChoice.equalsIgnoreCase("Pass") || pickSuitChoice.equalsIgnoreCase("Pick") || pickSuitChoice.equalsIgnoreCase("Pick up"))) {
                            System.out.println("Enter a valid action (Pass, Pick, Pick up)");
                            pickSuitChoice = userScanner.nextLine();
                            if (pickSuitChoice.equalsIgnoreCase("pass")) {
                                System.out.println(currentReciever.getName() + " passes");
                                break;
                            } else if(pickSuitChoice.equalsIgnoreCase("Pick") || pickSuitChoice.equalsIgnoreCase("Pick up")) {
                                System.out.println("Pick up: " + currentReciever.getName() + "\n");
                                trumpChooser = currentReciever;
                                picked = true;
                                trumpSuit = topCard.getSuit();
                                dealer.pickUpCard(topCard, trumpSuit);

                                while (!currentReciever.getName().equalsIgnoreCase(firstDealer.getName())) {
                                    currentReciever = dealerQueue.remove(0);
                                    dealerQueue.add(currentReciever);
                                }
                                break;
                            }
                        }
                        break;
                    }
                } else { //section to have computer possibly pick up the top card
                    String decision = currentReciever.pickOrPass(topCard, dealer);
                    if (decision.equalsIgnoreCase("Pick") || decision.equalsIgnoreCase("pick up")) {
                        //code for dealer to swap if computer or have player choose which card to drop
                        System.out.println("Pick up: " + currentReciever.getName() + "\n");
                        trumpChooser = currentReciever;
                        picked = true;
                        trumpSuit = topCard.getSuit();
                        dealer.pickUpCard(topCard, trumpSuit);

                        while (!currentReciever.getName().equalsIgnoreCase(firstDealer.getName())) {
                            currentReciever = dealerQueue.remove(0);
                            dealerQueue.add(currentReciever);
                        }
                        break;
                    } else {
                        System.out.println(currentReciever.getName() + " passes");
                    }
                }
            }

            //code to choose trump if no one wants to pick up the top card (the dealer has to if no one else will)
            if (picked == false) {
                String pickSuit = null;
                for (int currentIndex = 0; currentIndex < 4; currentIndex++) {
                    Player currentReciever = dealerQueue.remove(0);
                    dealerQueue.add(currentReciever);

                    //code to allow user to select a trump suit
                    if (currentReciever.getName().equalsIgnoreCase("user")) {
                        if (currentIndex == 3) {
                            System.out.println("Pick a trump suit");
                            pickSuitChoice = userScanner.nextLine();
                            while (!(pickSuitChoice.equalsIgnoreCase("Clubs")  || pickSuitChoice.equalsIgnoreCase("Diamonds")  || pickSuitChoice.equalsIgnoreCase("Hearts")  || pickSuitChoice.equalsIgnoreCase("Spades"))) {
                                System.out.println("Enter a valid card suit (Clubs, Diamonds, Hearts, or Spades), or pass");
                                pickSuitChoice = userScanner.nextLine();
                            }
                            trumpChooser = currentReciever;
                            trumpSuit = pickSuitChoice;
                            System.out.println(currentReciever.getName() + " chose trump");
                            while (!currentReciever.getName().equalsIgnoreCase(firstDealer.getName())) {
                                currentReciever = dealerQueue.remove(0);
                                dealerQueue.add(currentReciever);
                            }
                            break;
                        } else {
                            System.out.println("'Choose trump' or 'pass'");
                            pickSuitChoice = userScanner.nextLine();
                            if (!pickSuitChoice.equalsIgnoreCase("pass")) {
                                while (!(pickSuitChoice.equalsIgnoreCase("Clubs")  || pickSuitChoice.equalsIgnoreCase("Diamonds")  || pickSuitChoice.equalsIgnoreCase("Hearts")  || pickSuitChoice.equalsIgnoreCase("Spades"))) {
                                    System.out.println("Enter a valid card suit (Clubs, Diamonds, Hearts, or Spades), or pass");
                                    pickSuitChoice = userScanner.nextLine();
                                    if (pickSuitChoice.equalsIgnoreCase("pass")) {
                                        System.out.println(currentReciever.getName() + " passes");
                                        break;
                                    }
                                }
                                if (!pickSuitChoice.equalsIgnoreCase("pass")) {
                                    trumpChooser = currentReciever;
                                    trumpSuit = pickSuitChoice;
                                    System.out.println(currentReciever.getName() + " chose trump");
                                    while (!currentReciever.getName().equalsIgnoreCase(firstDealer.getName())) {
                                        currentReciever = dealerQueue.remove(0);
                                        dealerQueue.add(currentReciever);
                                    }
                                    break;
                                }
                            } else {
                                System.out.println(currentReciever.getName() + " passes");
                            }
                        }
                    } else { //code to have the computer possibly pick a trump suit
                        pickSuit = currentReciever.suitOrPass(dealer);
                        if (pickSuit != null && !pickSuit.equalsIgnoreCase("Pass")) {
                            System.out.println(currentReciever.getName() + " chose trump");
                            trumpChooser = currentReciever;
                            trumpSuit = pickSuit;
                            while (!currentReciever.getName().equalsIgnoreCase(firstDealer.getName())) {
                                currentReciever = dealerQueue.remove(0);
                                dealerQueue.add(currentReciever);
                            }
                            break;
                        } else {
                            System.out.println(currentReciever.getName() + " passes");
                        }
                    }
                }
            }

            Player currentPlayer = null;
            int userChoice = 0;
            //start of actually playing the game, 5 Tricks for each deal
            for (int trickNumber = 0; trickNumber < 5; trickNumber++) {
                String suitLed = "empty";
                int turnIndex = 0;
                Card[] cardsPlayed = new Card[4];
                boolean hasLeadSuit;

                System.out.println("Trump suit is "+ trumpSuit + "\n");
                //code to run through queue and have each player play a card
                for (int playerIndex = 0; playerIndex < 4; playerIndex++) {
                    if (playerIndex == 1) {
                        suitLed = getCardSuit(cardsPlayed[0], trumpSuit);
                    }
                    currentPlayer = dealerQueue.remove(0);
                    if (!currentPlayer.getName().equalsIgnoreCase("user")) { //have computer choose a card to play
                        sleep(1500);
                        cardsPlayed[playerIndex] = currentPlayer.chooseCardToPlay(cardsPlayed, trumpSuit, playerIndex);
                        System.out.println(currentPlayer.getName() + " played " + cardsPlayed[playerIndex].getFaceValue() + " of " + cardsPlayed[playerIndex].getSuit());
                    } else {
                        sleep(1500);
                        hasLeadSuit = false;
                        boolean playedValidSuit = false;
                        while(playedValidSuit == false) {
                            System.out.println("User, enter card index to play");
                            if (playerIndex != 0) {
                                hasLeadSuit = currentPlayer.hasLeadSuit(suitLed, trumpSuit);
                            }

                            currentPlayer.printCurrentHand();
                            System.out.print("Card selection: ");
                            userChoice = userScanner.nextInt();
                            while (!(userChoice < currentUser.getHand().size() + 1 && userChoice > 0)) {
                                System.out.println("Invalid index, please select a card index from the list printed above");
                                System.out.println("hand size: " + currentUser.getHand().size());
                                System.out.println("User, enter card index to play");
                                currentPlayer.printCurrentHand();
                                System.out.print("Card selection: ");
                                userChoice = userScanner.nextInt();
                            }
                            if (hasLeadSuit == true) { // section to force player to play the lead suit if present
                                playedValidSuit = followSuitCheck(suitLed, currentPlayer.peekAtCardIndex(userChoice - 1), trumpSuit);
                            } else {
                                playedValidSuit = true;
                            }
                            if (playedValidSuit) {
                                break;
                            } else {
                                System.out.println("Invalid card choice, remember you must play a card that has the same\nsuit as the first card played if you have it");
                            }
                        }
                        cardsPlayed[playerIndex] = currentPlayer.removeFromHand(userChoice-1);
                        System.out.println("\n" + currentPlayer.getName() + " played " + cardsPlayed[playerIndex].getFaceValue() + " of " + cardsPlayed[playerIndex].getSuit());

                        }
                    dealerQueue.add(currentPlayer);
                }

                //get the index of the current trick winner
                int winnerIndex = getTrickWinner(trumpSuit, cardsPlayed);

                for (int playerIndex = 0; playerIndex <= winnerIndex; playerIndex++) {
                    currentPlayer = dealerQueue.remove(0);
                    dealerQueue.add(currentPlayer);
                }
                if (currentPlayer.getName().equalsIgnoreCase("user") || currentPlayer.getName().equalsIgnoreCase("userPartner")) {
                    userTrickScore++;
                    System.out.println("\nUSER TEAM POINT\n");
                }
                if (currentPlayer.getName().equalsIgnoreCase("opponent1") || currentPlayer.getName().equalsIgnoreCase("opponent2")) {
                    oppTrickScore++;
                    System.out.println("\nOPPONENT TEAM POINT\n");
                }
                currentPlayer = dealerQueue.remove(dealerQueue.size()-1);
                dealerQueue.add(0, currentPlayer);
                sleep(2000);
            }

            //rotates the queue so that the correct dealer is in place
            while (!currentPlayer.getName().equalsIgnoreCase(firstDealer.getName())) {
                currentPlayer = dealerQueue.remove(0);
                dealerQueue.add(currentPlayer);
            }

            //tracks overall score for each player pair and score for each round
            if (userTrickScore > oppTrickScore) {
                if (trumpChooser.getName().equalsIgnoreCase("user") || trumpChooser.getName().equalsIgnoreCase("userPartner")) {
                    if (userTrickScore == 5) {
                        userScore+=2;
                    } else {
                        userScore++;
                    }
                } else {
                    if (userTrickScore == 5) {
                        userScore+=4;
                    } else {
                        userScore+=2;
                    }
                }
                System.out.println("User team won that round!");
            } else {
                if (trumpChooser.getName().equalsIgnoreCase("opponent1") || trumpChooser.getName().equalsIgnoreCase("opponent2")) {
                    if (oppTrickScore == 5) {
                        opponentScore+=2;
                    } else {
                        opponentScore++;
                    }
                } else {
                    if (oppTrickScore == 5) {
                        opponentScore+=4;
                    } else {
                        opponentScore+=2;
                    }
                }
                System.out.println("Opponent team won that round!");
            }
            System.out.println("Current Score: \nUser Team: " + userScore + "\nOpponent Team: " + opponentScore);

        }

        //stops the game if a team wins and prints the final score and winner
        if (userScore >= 10) {
            System.out.println("\nCongrats you have won this game of Euchre!");
        } else if (opponentScore >= 10) {
            System.out.println("\nYour opponent's won, better luck next time");
        }
        System.out.println("\nFinal Score: \nUser Team " + userScore + "\nOpponentScore " + opponentScore);
    }
}
