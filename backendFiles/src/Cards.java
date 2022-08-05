public class Cards {
    private int cardValue;
    private String faceValue;

    public Cards(int cardValue) {
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
}
