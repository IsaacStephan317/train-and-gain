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

    public void addToHand(Card requestedCard) {
        hand.add(requestedCard);
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

        if (!dealer.getName().equals(partner) || !dealer.getName().equals(name)) {
            dealingTeam = false;
        } else {
            dealingTeam = true;
        }
        //1 is clubs, 2 diamonds, 3 hearts, 4 spades
        int[] suits = new int[4];
        int[] totalCardValue = new int[4];
        int[] avgCardValue = new int[4];

        for (Card currCard: hand) {
            if (currCard.getSuit().equals("Clubs")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 0);
            } else if (currCard.getSuit().equals("Diamonds")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 1);
            } else if (currCard.getSuit().equals("Hearts")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 2);
            } else if (currCard.getSuit().equals("Spades")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 3);
            }
        }
        int trumpIndex = 0;
        if (topCard.getSuit().equals("Clubs")) {
            decisionScore = avgCardValue[0] + 1;
            trumpIndex = 0;
        } else if (topCard.getSuit().equals("Diamonds")) {
            decisionScore = avgCardValue[1] + 1;
            trumpIndex = 1;
        } else if (topCard.getSuit().equals("Hearts")) {
            decisionScore = avgCardValue[2] + 1;
            trumpIndex = 2;
        } else if (topCard.getSuit().equals("Spades")) {
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

        if (decisionScore >= 12 && suits[trumpIndex] >= 3) {
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
            if (currCard.getSuit().equals(trumpSuit)) {
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
            if (currCard.getSuit().equals("Clubs")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 0);
            } else if (currCard.getSuit().equals("Diamonds")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 1);
            } else if (currCard.getSuit().equals("Hearts")) {
                adjustDecisionScore(suits, totalCardValue, avgCardValue, currCard, 2);
            } else if (currCard.getSuit().equals("Spades")) {
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
        if (dealer.getName().equals(name)) {
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
                if (currCard.getSuit().equals(trumpSuit)) {
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
            int leadCard = 0;
            leadSuit = cardsPlayed[0].getSuit();
            boolean leadSuitFound = false;

            for (Card playedCard: cardsPlayed) {
                if (playedCard == null) {
                    break;
                }
                if (playedCard.getSuit().equals(trumpSuit)) {
                    playedCardsValue[count] += 15 + playedCard.getCardValue();
                } else {
                    playedCardsValue[count] += playedCard.getCardValue();
                }
                if (playedCardsValue[count] > playedCardsValue[leadCard]) {
                    leadCard = count;
                }
            }

            count = 0;
            for (Card currCard: hand) { //still need to add in logic for the right and the left (jacks that are trump)
                if (currCard.getSuit().equals(leadSuit)) {
                    handScores[count] += 100;
                    leadSuitFound = true;
                }
                if (currCard.getSuit().equals(trumpSuit)) {
                    handScores[count] += 15 + currCard.getCardValue();
                } else {
                    handScores[count] += currCard.getCardValue();
                }

                if (handScores[count] > handScores[bestCardIndex]) {
                    bestCardIndex = count;
                } else if(handScores[count] < handScores[bestCardIndex]) {
                    worstCardIndex = count;
                }
            }
            if (leadSuitFound == true || handScores[bestCardIndex] > playedCardsValue[leadCard]) {
                playedCardIndex = bestCardIndex;
            } else {
                playedCardIndex = worstCardIndex;
            }
        }

        return removeFromHand(playedCardIndex);
    }
}
