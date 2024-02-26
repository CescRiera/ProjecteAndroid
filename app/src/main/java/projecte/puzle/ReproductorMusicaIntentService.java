package projecte.puzle;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;

public class ReproductorMusicaIntentService extends IntentService {
    private static final String ACTION_REPRODUCIR = "projecte.puzle.action.REPRODUCIR";
    private static final String ACTION_PAUSAR = "projecte.puzle.action.PAUSAR";
    private static final String ACTION_INICIALIZAR = "projecte.puzle.action.INICIALIZAR";
    private static final String ACTION_ALLIBERAR = "projecte.puzle.action.ALLIBERAR";

    private static final String EXTRA_RECURSO_MUSICA = "projecte.puzle.extra.RECURSO_MUSICA";
    private static final String EXTRA_BOTO_ALTERNAR = "projecte.puzle.extra.BOTO_ALTERNAR";
    private static final String ACTION_REANUDAR = "projecte.puzle.action.REANUDAR";

    private static MediaPlayer mediaPlayer;
    private static boolean musicaReproduint = false;

    public ReproductorMusicaIntentService() {
        super("ReproductorMusicaIntentService");
    }

    public static void iniciarMusica(Context context, int recursoMusica) {
        Intent intent = new Intent(context, ReproductorMusicaIntentService.class);
        intent.setAction(ACTION_INICIALIZAR);
        intent.putExtra(EXTRA_RECURSO_MUSICA, recursoMusica);
        context.startService(intent);
    }

    public static void alternarMusica(Context context, Button botoAlternarMusica) {
        Intent intent = new Intent(context, ReproductorMusicaIntentService.class);
        if (musicaReproduint) {
            intent.setAction(ACTION_PAUSAR);
        } else {
            intent.setAction(ACTION_REPRODUCIR);
        }
        intent.putExtra(EXTRA_BOTO_ALTERNAR, (CharSequence) botoAlternarMusica);
        context.startService(intent);
    }

    public static void alliberar(Context context) {
        Intent intent = new Intent(context, ReproductorMusicaIntentService.class);
        intent.setAction(ACTION_ALLIBERAR);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d("klk",""+ action);
            if (ACTION_REPRODUCIR.equals(action)) {
                handleReproducirMusica((Button) intent.getSerializableExtra(EXTRA_BOTO_ALTERNAR));
            } else if (ACTION_PAUSAR.equals(action)) {
                handlePausarMusica((Button) intent.getSerializableExtra(EXTRA_BOTO_ALTERNAR));
            } else if (ACTION_INICIALIZAR.equals(action)) {
                handleInicializarMusica(intent.getIntExtra(EXTRA_RECURSO_MUSICA, 0));
            } else if (ACTION_ALLIBERAR.equals(action)) {
                handleAlliberarMusica();
            }
        }
    }

    private void handleReproducirMusica(Button botoAlternarMusica) {
        if (mediaPlayer != null && !musicaReproduint) {
            mediaPlayer.start();
            musicaReproduint = true;
            actualizarTextoBoton(botoAlternarMusica, "Pausar música");
        }
    }

    private void handlePausarMusica(Button botoAlternarMusica) {
        if (mediaPlayer != null && musicaReproduint) {
            mediaPlayer.pause();
            musicaReproduint = false;
            actualizarTextoBoton(botoAlternarMusica, "Reproduir música");
        }
    }

    private void handleInicializarMusica(int recursoMusica) {
        mediaPlayer = MediaPlayer.create(this, recursoMusica);
        mediaPlayer.setLooping(true);
    }

    private void handleAlliberarMusica() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void actualizarTextoBoton(Button boton, String texto) {
        if (boton != null) {
            boton.setText(texto);
        }
    }

    public static void pausarMusica(Context context) {
        Intent intent = new Intent(context, ReproductorMusicaIntentService.class);
        intent.setAction(ACTION_PAUSAR);
        context.startService(intent);
    }

    public static void reanudarMusica(Context context) {
        Intent intent = new Intent(context, ReproductorMusicaIntentService.class);
        intent.setAction(ACTION_REANUDAR);
        context.startService(intent);

    }
}
