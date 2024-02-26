package projecte.puzle;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Puntuacio implements Serializable {
    private int score;
    private String data;

    public Puntuacio() {
    }

    public Puntuacio(int score) {
        this.score = score;
    }
    public Puntuacio(int score, Date data) {
        this.score = score;
        setData(data);
    }

    public int getScore() {
        return score;
    }
    public String getData() {
        return data;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void setData(Date data) {
        SimpleDateFormat sz = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.data = sz.format(data);
    }

    public String getPuntuacion() {
        return "Cantitat de moviments: "+score+"\nData: "+data;
    }
}
