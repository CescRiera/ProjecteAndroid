package projecte.puzle;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;

public class GestorReproductorMusica {
    private static MediaPlayer mediaPlayer;
    private static boolean musicaReproduint = false;

    public static void inicialitzar(Context context, int recursoMusica) {
        mediaPlayer = MediaPlayer.create(context, recursoMusica);
        mediaPlayer.setLooping(true);
    }
    public static boolean getMusicState(){
        return musicaReproduint;
    }

    public static void alternarMusica(Button botoAlternarMusica) {
        if (musicaReproduint) {
            pausarMusica(botoAlternarMusica);
        } else {
            reproduirMusica(botoAlternarMusica);
        }
    }

    private static void reproduirMusica(Button botoAlternarMusica) {
        mediaPlayer.start();
        musicaReproduint = true;
        if (botoAlternarMusica != null) {
            botoAlternarMusica.setText("Pausar música");
        }
    }

    private static void pausarMusica(Button botoAlternarMusica) {
        mediaPlayer.pause();
        musicaReproduint = false;
        if (botoAlternarMusica != null) {
            botoAlternarMusica.setText("Reproduir música");
        }
    }


    public static void alliberar() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
