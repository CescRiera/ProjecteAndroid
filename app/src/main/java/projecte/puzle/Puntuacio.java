package projecte.puzle;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Clase Puntuacion que representa la puntuación de un artista
 * @version 1.0 22.02.2024
 */

@IgnoreExtraProperties
public class Puntuacio {

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
