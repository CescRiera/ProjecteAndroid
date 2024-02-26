package projecte.puzle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PantallaInicial extends AppCompatActivity {
    GestorReproductorMusica grm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inici);

        Button btnNivel1 = findViewById(R.id.btnNivel1);
        Button btnNivel2 = findViewById(R.id.btnNivel2);
        Button btnNivel3 = findViewById(R.id.btnNivel3);

        btnNivel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMainActivity(2); // Nivel 1: 2 filas y 2 columnas
            }
        });

        btnNivel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMainActivity(3); // Nivel 2: 3 filas y 3 columnas
            }
        });

        btnNivel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMainActivity(4); // Nivel 3: 4 filas y 4 columnas
            }
        });

        Button botoAlternarMusica = findViewById(R.id.botoAlternarMusica);
        botoAlternarMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMusic();
            }
        });
    }

    private void abrirMainActivity(int dimensiones) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("dimensiones", dimensiones); // Pasar el número de filas y columnas como extra
        startActivity(intent);
        finish(); // Cerrar la actividad actual
    }

    private void toggleMusic() {
        Intent intent = new Intent(this, SoundService.class);

        Log.d("klk",""+GestorReproductorMusica.getMusicState());
        if (grm.getMusicState()) {
            // Detener la música
            stopService(intent);
        } else {
            // Iniciar la música
            startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GestorReproductorMusica.alliberar();
    }
}
