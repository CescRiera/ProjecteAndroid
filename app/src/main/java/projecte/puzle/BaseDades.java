package projecte.puzle;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BaseDades extends AppCompatActivity {

    DatabaseReference DBArtistes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No se utiliza layout
        // setContentView(R.layout.m18_firebase);

        DBArtistes = FirebaseDatabase.getInstance().getReference("artistes");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Solo se añade un listener, no hay interacción con vistas
        DBArtistes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Aquí puedes manejar los datos recibidos de la base de datos
                // por ejemplo, mostrarlos en un TextView o cualquier otra acción
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar error de cancelación
            }
        });
    }

    private void afegirScore(String id, int score) {
        // No se utilizan vistas, solo se añade información a la base de datos
        DBArtistes.child(id).child("score").setValue(score);
        Toast.makeText(this, "Score añadido", Toast.LENGTH_LONG).show();
    }
}

