package projecte.puzle;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import projecte.puzle.MainActivity;

public class PantallaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicial);

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
    }


    private void abrirMainActivity(int dimensiones) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("dimensiones", dimensiones); // Pasar el número de filas y columnas como extra
        startActivity(intent);
        finish(); // Cerrar la actividad actual
    }
}
