public class Card {
    private int cardValue;
    private String faceValue;

    public Card(int cardValue) {
        this.cardValue = cardValue;
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
}
