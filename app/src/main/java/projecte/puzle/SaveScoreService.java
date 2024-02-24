package projecte.puzle;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SaveScoreService extends IntentService {

    public SaveScoreService() {
        super("SaveScoreService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int score = intent.getIntExtra("score", 0);
            guardarPuntuacionEnRealtimeDatabase(score);
        }
    }

    private void guardarPuntuacionEnRealtimeDatabase(int puntuacion) {
        // Aquí colocas la lógica para guardar la puntuación en la base de datos en tiempo real
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("puntuaciones");
        String nuevaClave = databaseReference.push().getKey();
        Puntuacio puntuacio = new Puntuacio(puntuacion);
        databaseReference.child(nuevaClave).setValue(puntuacio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Éxito
                        // Puedes enviar una notificación de éxito si lo deseas
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error
                        // Puedes enviar una notificación de error si lo deseas
                    }
                });
    }
}
