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
                suits[0]++;
                if (currCard.getCardValue() == 11) {
                    totalCardValue[0] += 15;
                } else {
                    totalCardValue[0] += currCard.getCardValue();
                }
                avgCardValue[0] = totalCardValue[0]/suits[0];

            } else if (currCard.getSuit().equals("Diamonds")) {
                suits[1]++;
                if (currCard.getCardValue() == 11) {
                    totalCardValue[1] += 15;
                } else {
                    totalCardValue[1] += currCard.getCardValue();
                }
                avgCardValue[1] = totalCardValue[1]/suits[1];

            } else if (currCard.getSuit().equals("Hearts")) {
                suits[2]++;
                if (currCard.getCardValue() == 11) {
                    totalCardValue[2] += 15;
                } else {
                    totalCardValue[2] += currCard.getCardValue();
                }
                avgCardValue[2] = totalCardValue[1]/suits[2];

            } else if (currCard.getSuit().equals("Spades")) {
                suits[3]++;
                if (currCard.getCardValue() == 11) {
                    totalCardValue[3] += 15;
                } else {
                    totalCardValue[3] += currCard.getCardValue();
                }
                avgCardValue[3] = totalCardValue[3]/suits[3];

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
        if (topCard.getCardValue() == 11 || topCard.getCardValue() == 13 || topCard.getCardValue() == 14) {
            if (dealingTeam) {
                decisionScore++;
            } else {
                decisionScore--;
            }
        }

        if (decisionScore >= 12.5 && suits[trumpIndex] >= 3) {
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

    public String suitOrPass(Player dealer) {
        int decisionScore = 0;
        String wouldBeSuit;

        //0 is clubs, 1 diamonds, 2 hearts, 3 spades
        int[] suits = new int[4];
        int[] totalCardValue = new int[4];
        int[] avgCardValue = new int[4];
        for (Card currCard: hand) {
            if (currCard.getSuit().equals("Clubs")) {
                suits[0]++;
                if (currCard.getCardValue() == 11) {
                    totalCardValue[0] += 15;
                } else {
                    totalCardValue[0] += currCard.getCardValue();
                }
                avgCardValue[0] = totalCardValue[0]/suits[0];
            } else if (currCard.getSuit().equals("Diamonds")) {
                suits[1]++;
                if (currCard.getCardValue() == 11) {
                    totalCardValue[1] += 15;
                } else {
                    totalCardValue[1] += currCard.getCardValue();
                }
                avgCardValue[1] = totalCardValue[1]/suits[1];
            } else if (currCard.getSuit().equals("Hearts")) {
                suits[2]++;
                if (currCard.getCardValue() == 11) {
                    totalCardValue[2] += 15;
                } else {
                    totalCardValue[2] += currCard.getCardValue();
                }
                avgCardValue[2] = totalCardValue[2]/suits[2];
            } else if (currCard.getSuit().equals("Spades")) {
                suits[3]++;
                if (currCard.getCardValue() == 11) {
                    totalCardValue[3] += 15;
                } else {
                    totalCardValue[3] += currCard.getCardValue();
                }
                avgCardValue[3] = totalCardValue[3]/suits[3];
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
}
