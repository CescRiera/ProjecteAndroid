package projecte.puzle;

import java.io.Serializable;

public class Puntuacio implements Serializable {
    private int score;

    public Puntuacio() {
    }

    public Puntuacio(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
