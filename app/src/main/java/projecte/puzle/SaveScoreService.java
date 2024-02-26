package projecte.puzle;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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
        Log.d("SaveScoreService", "Servicio SaveScoreService iniciado correctamente.");
        if (intent != null) {
            Puntuacio puntuacio = (Puntuacio) intent.getSerializableExtra("puntuacion");
            if (puntuacio != null) {
                guardarPuntuacionEnRealtimeDatabase(puntuacio);
                Log.d("klk","P: "+puntuacio.getScore());
            }
        }
    }




    private void guardarPuntuacionEnRealtimeDatabase(Puntuacio puntuacio) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("puntuaciones");
        String nuevaClave = databaseReference.push().getKey();
        databaseReference.child(nuevaClave).setValue(puntuacio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Éxito
                        Log.d("klk", "Puntuación guardada exitosamente en la base de datos.");
                        // Puedes enviar una notificación de éxito si lo deseas
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error
                        Log.e("klk", "Error al guardar la puntuación en la base de datos: " + e.getMessage());
                        // Puedes enviar una notificación de error si lo deseas
                    }
                });
    }





}
