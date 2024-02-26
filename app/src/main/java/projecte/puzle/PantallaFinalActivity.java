package projecte.puzle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaFinalActivity extends AppCompatActivity {
    Button btnVolverInicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_final);

        // Obtener la puntuación de los extras del Intent
        Puntuacio puntuacio = (Puntuacio) getIntent().getSerializableExtra("puntuacion");

        // Mostrar la puntuación en un TextView u otro componente de la interfaz de usuario
        if (puntuacio != null) {
            TextView tvPuntuacion = findViewById(R.id.textViewPuntuacion);
            if (tvPuntuacion != null) {
                tvPuntuacion.setText("Puntuación:\n " + puntuacio.getPuntuacion());
            }
        }

        btnVolverInicio = findViewById(R.id.btnVolverInicio);
        btnVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaFinalActivity.this, PantallaInicial.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
