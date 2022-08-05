public class Card {
    private int cardValue;
    private String faceValue;
    private String suit;

    public Card(int cardValue, int suit) {
        this.cardValue = cardValue;
        switch(suit) {
            case 1:
                this.suit = "Clubs";
                break;
            case 2:
                this.suit = "Diamonds";
                break;
            case 3:
                this.suit = "Hearts";
                break;
            case 4:
                this.suit = "Spades";
                break;
        }
        if (cardValue > 10) {
            if (cardValue == 11) {
                faceValue = "Jack";
            } else if (cardValue == 12) {
                faceValue = "Queen";
            } else if (cardValue == 13) {
                faceValue = "King";
            } else {
                faceValue = "Ace";
            }
        } else {
            faceValue = String.valueOf(cardValue);
        }
    }

    public int getCardValue() {
        return cardValue;
    }

    public String getFaceValue() {
        return faceValue;
    }

    public String getSuit() {
        return suit;
    }
}
