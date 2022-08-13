import java.util.ArrayList;

public class Player {
    private ArrayList<Card> hand = new ArrayList<>();
    private int playerScore;
    private String partner;
    private String name;

    public Player(String name, String partner) {
        playerScore = 0;
        this.name = name;
        this.partner = partner;
    }

    public int getUserScore() {
        return playerScore;
    }

    public void increaseUserScore(int roundWinnings) {
        playerScore += roundWinnings;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public Card removeFromHand(int requestedCard) {

        return hand.remove(requestedCard);
    }

    public String getName() {
        return name;
    }

    public String getPartner() {
        return partner;
    }

    public void printCurrentHand() {
        int index = 1;
        System.out.println("\nCurrent hand:");
        for (Card currentCard: hand) {
            System.out.println(index + ": " + currentCard.getFaceValue() + " of " + currentCard.getSuit());
            index++;
        }
    }

    public void addToHand(Card requestedCard) {
        hand.add(requestedCard);
    }

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

    private void adjustDecisionScore(int[] suits, int[] totalCardValue, int[] avgCardValue, Card currCard, int index) {
        suits[index]++;
        if (currCard.getCardValue() == 11) {
            totalCardValue[index] += 15;
        } else {
            totalCardValue[index] += currCard.getCardValue();
        }
        avgCardValue[index] = totalCardValue[index]/suits[index];
    }

    public String pickOrPass(Card topCard, Player dealer) {
        String topSuit = topCard.getSuit();
        Boolean dealingTeam;
        int decisionScore = 0;

        if (!dealer.getName().equalsIgnoreCase(partner) || !dealer.getName().equalsIgnoreCase(name)) {
            dealingTeam = false;
        } else {
            dealingTeam = true;
        }
        //1 is clubs, 2 diamonds, 3 hearts, 4 spades
        int[] suits = new int[4];
        int[] totalCardValue = new int[4];
        int[] avgCardValue = new int[4];

        for (Card currCard: hand) {
            if (currCard.getSuit().equalsIgnoreCase("Clubs")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 0);
            } else if (currCard.getSuit().equalsIgnoreCase("Diamonds")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 1);
            } else if (currCard.getSuit().equalsIgnoreCase("Hearts")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 2);
            } else if (currCard.getSuit().equalsIgnoreCase("Spades")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 3);
            }
        }
        int trumpIndex = 0;
        if (topCard.getSuit().equalsIgnoreCase("Clubs")) {
            decisionScore = avgCardValue[0] + 1;
            trumpIndex = 0;
        } else if (topCard.getSuit().equalsIgnoreCase("Diamonds")) {
            decisionScore = avgCardValue[1] + 1;
            trumpIndex = 1;
        } else if (topCard.getSuit().equalsIgnoreCase("Hearts")) {
            decisionScore = avgCardValue[2] + 1;
            trumpIndex = 2;
        } else if (topCard.getSuit().equalsIgnoreCase("Spades")) {
            decisionScore = avgCardValue[3] + 1;
            trumpIndex = 3;
        }
        suits[trumpIndex]++;

        if (topCard.getCardValue() == 11 || topCard.getCardValue() == 13 || topCard.getCardValue() == 14) {
            if (dealingTeam) {
                decisionScore++;
            } else {
                decisionScore--;
            }
        }

        if (decisionScore >= 14 && suits[trumpIndex] >= 3) {
            return "Pick";
        } else {
            return "Pass";
        }
    }

    private String getSuit(int place) {
        //0 is clubs, 1 diamonds, 2 hearts, 3 spades
        if (place == 0) {
            return "Clubs";
        } else if (place == 1) {
            return "Diamonds";
        } else if (place == 2) {
            return "Hearts";
        } else if (place == 3) {
            return "Spades";
        } else {
            return "error";
        }
    }

    //still need to factor in the jack that doesn't have the same suit as trump but counts as trump
    public void pickUpCard (Card topCard, String trumpSuit) {
        int[] handScores = new int[5];
        int count = 0;
        int currentWorst = 0;
        for (Card currCard: hand) {
            if (currCard.getSuit().equalsIgnoreCase(trumpSuit)) {
                handScores[count] += 15 + currCard.getCardValue();
            } else {
                handScores[count] += currCard.getCardValue();
            }
            if (handScores[count] < handScores[currentWorst]) {
                currentWorst = count;
            }
            count++;
        }
        hand.remove(currentWorst);
        hand.add(topCard);
    }

    public String suitOrPass(Player dealer) {
        int decisionScore = 0;
        String wouldBeSuit;

        //0 is clubs, 1 diamonds, 2 hearts, 3 spades
        int[] suits = new int[4];
        int[] totalCardValue = new int[4];
        int[] avgCardValue = new int[4];
        for (Card currCard: hand) {
            if (currCard.getSuit().equalsIgnoreCase("Clubs")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 0);
            } else if (currCard.getSuit().equalsIgnoreCase("Diamonds")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 1);
            } else if (currCard.getSuit().equalsIgnoreCase("Hearts")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 2);
            } else if (currCard.getSuit().equalsIgnoreCase("Spades")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 3);
            }
        }

        int maxTotal = 0;
        int trumpIndex = 0;
        for (int i = 0; i < avgCardValue.length; i++) {
            if (totalCardValue[i] > maxTotal) {
                maxTotal = totalCardValue[i];
                decisionScore = avgCardValue[i];
                trumpIndex = i;
            }
        }

        wouldBeSuit = getSuit(trumpIndex);
        if (dealer.getName().equalsIgnoreCase(name)) {
            return wouldBeSuit;
        } else {
            if (decisionScore >= 12 && suits[trumpIndex] >= 3) {
                return wouldBeSuit;
            } else {
                return "Pass";
            }
        }

    }

    public Card chooseCardToPlay(Card[] cardsPlayed, String trumpSuit, int numbPlayed) {
        int playedCardIndex = 0;
        int bestCardIndex = 0;
        int worstCardIndex = 0;
        int followSuitIndex = 0;
        int count = 0;
        int[] handScores = new int[5];
        String leadSuit = "empty";

        //need to add in logic to follow suit and include other jack as a trump
        if (numbPlayed == 0) {
            for (Card currCard: hand) {
                if (currCard.getSuit().equalsIgnoreCase(trumpSuit)) {
                    handScores[count] += currCard.getCardValue();
                } else {
                    handScores[count] += 4 + currCard.getCardValue();
                }
                if (handScores[count] > handScores[bestCardIndex]) {
                    bestCardIndex = count;
                }
                count++;
            }
            playedCardIndex = bestCardIndex;
        } else {
            int[] playedCardsValue = new int[4];
            int topCard = 0;
            Card leadCard = cardsPlayed[0];

            if (leadCard.getFaceValue().equalsIgnoreCase("Jack")) {
                if (getJackPair(trumpSuit, leadCard)) {
                    leadSuit = trumpSuit;
                }
            } else {
                leadSuit = cardsPlayed[0].getSuit();
            }
            boolean leadSuitFound = false;

            for (Card playedCard: cardsPlayed) {
                if (playedCard == null) {
                    break;
                }
                if (playedCard.getSuit().equalsIgnoreCase(trumpSuit)) {
                    playedCardsValue[count] += 15 + playedCard.getCardValue();
                } else {
                    playedCardsValue[count] += playedCard.getCardValue();
                }
                if (playedCardsValue[count] > playedCardsValue[topCard]) {
                    topCard = count;
                }
                count++;
            }

            count = 0;
            for (Card currCard: hand) { //still need to add in logic for the right and the left (jacks that are trump)
                if (currCard.getSuit().equalsIgnoreCase(leadSuit)) {
                    handScores[count] += 100;
                    leadSuitFound = true;
                }
                if (currCard.getSuit().equalsIgnoreCase(trumpSuit)) {
                    handScores[count] += 15 + currCard.getCardValue();
                } else {
                    handScores[count] += currCard.getCardValue();
                }

                if (handScores[count] > handScores[bestCardIndex]) {
                    bestCardIndex = count;
                } else if(handScores[count] < handScores[bestCardIndex]) {
                    worstCardIndex = count;
                }
                count++;
            }
            if (leadSuitFound == true || handScores[bestCardIndex] > playedCardsValue[topCard]) {
                playedCardIndex = bestCardIndex;
            } else {
                playedCardIndex = worstCardIndex;
            }
        }

        return removeFromHand(playedCardIndex);
    }
}
