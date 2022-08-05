import java.util.ArrayList;

public class UserPlayer {
    ArrayList<Integer> hand = new ArrayList<>();
    public int userScore;

    public UserPlayer() {
        userScore = 0;
    }

    public int getUserScore() {
        return userScore;
    }

    public void increaseUserScore(int roundWinnings) {
        userScore += roundWinnings;
    }

    public int removeFromHand() {
        return 0;
    }

    public void addToHand() {

    }
}
