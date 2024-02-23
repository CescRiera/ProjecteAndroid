package projecte.puzle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    private ImageView selectedImageView = null;
    private Button botoAlternarMusica;
    private int movimientoCounter = 0;
    private TextView score;
    private int rows, cols; // Variables para filas y columnas
    private Button btnVolverInicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Dentro del método onCreate() de tu actividad MainActivity
        firestore = FirebaseFirestore.getInstance();

        btnVolverInicio = findViewById(R.id.btnVolverInicio);
        btnVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para volver a la actividad PantallaInicial
                Intent intent = new Intent(MainActivity.this, PantallaInicial.class);
                // Iniciar la actividad PantallaInicial
                startActivity(intent);
                // Finalizar la actividad actual (MainActivity)
                finish();
            }
        });



        Intent intent = getIntent();
        int dimensiones = intent.getIntExtra("dimensiones", 0); // Obtener el número de filas y columnas del extra
        if (dimensiones == 0) {
            // Si no se pasa el número de filas y columnas, salir de la actividad
            Toast.makeText(this, "Error: Dimensiones no válidas", Toast.LENGTH_SHORT).show();
            finish();
        }

        rows = cols = dimensiones; // Establecer el número de filas y columnas

        score = findViewById(R.id.cmpt);
        GestorReproductorMusica.inicialitzar(this, R.raw.m02_audio1);
        botoAlternarMusica = findViewById(R.id.botoAlternarMusica);
        botoAlternarMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GestorReproductorMusica.alternarMusica(botoAlternarMusica);
            }
        });

        Bitmap originalImage = BitmapFactory.decodeResource(getResources(), R.drawable.pb);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        int screenWidth = 1080; // Ancho de la pantalla en píxeles
        int screenHeight = 1920; // Alto de la pantalla en píxeles

        int cellWidth = screenWidth / cols;
        int cellHeight = screenHeight / rows;

        gridLayout.setColumnCount(cols);
        gridLayout.setRowCount(rows);

        Bitmap[][] parts = SplitImatge.divideImage(originalImage, rows, cols, cellWidth, cellHeight);

        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < rows * cols; i++) {
            positions.add(i);
        }

        Collections.shuffle(positions);

        int positionIndex = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int position = positions.get(positionIndex++);
                int row = position / cols;
                int col = position % cols;

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(cellWidth, cellHeight));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                if (position == rows * cols - 1) {
                    imageView.setImageBitmap(Bitmap.createBitmap(cellWidth, cellHeight, Bitmap.Config.ARGB_8888));
                } else {
                    imageView.setImageBitmap(parts[row][col]);
                }

                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(cellWidth, cellHeight));
                frameLayout.setPadding(4, 4, 4, 4);
                frameLayout.addView(imageView);


                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView clickedImageView = (ImageView) v;
                        int clickedPosition = gridLayout.indexOfChild((View) clickedImageView.getParent());

                        if (selectedImageView != null) {
                            int selectedPosition = gridLayout.indexOfChild((View) selectedImageView.getParent());

                            int selectedRow = selectedPosition / cols;
                            int selectedCol = selectedPosition % cols;
                            int clickedRow = clickedPosition / cols;
                            int clickedCol = clickedPosition % cols;


                            if ((Math.abs(selectedRow - clickedRow) == 1 && selectedCol == clickedCol) ||
                                    (Math.abs(selectedCol - clickedCol) == 1 && selectedRow == clickedRow)) {


                                // Si las celdas están adyacentes, intercambia las imágenes
                                Bitmap tempBitmap = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();
                                selectedImageView.setImageBitmap(((BitmapDrawable) clickedImageView.getDrawable()).getBitmap());
                                clickedImageView.setImageBitmap(tempBitmap);
                                selectedImageView.clearColorFilter();
                                selectedImageView = null;
                                movimientoCounter++;
                                score.setText("Movimientos: " + movimientoCounter);
                                guardarPuntuacionEnRealtimeDatabase(movimientoCounter);


                                // Guardar el puntaje en Firestore aquí
                            }
                        } else {
                            // Si no hay ninguna imagen seleccionada, selecciona la imagen clicada
                            selectedImageView = clickedImageView;
                            ColorMatrix matrix = new ColorMatrix();
                            matrix.setSaturation(0);
                            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                            selectedImageView.setColorFilter(filter); // Cambiar color al clicar
                        }
                    }

                });
                gridLayout.addView(frameLayout);
            }
        }
    }
    private void guardarPuntuacionEnRealtimeDatabase(int puntuacion) {
        // Obtener la referencia de la base de datos en tiempo real
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("puntuaciones");

        // Generar una nueva clave única para la puntuación
        String nuevaClave = databaseReference.push().getKey();

        // Crear un objeto Puntuacio con la puntuación proporcionada
        Puntuacio puntuacio = new Puntuacio(puntuacion);

        // Guardar la puntuación en la base de datos en tiempo real bajo la nueva clave
        databaseReference.child(nuevaClave).setValue(puntuacio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Éxito
                        Toast.makeText(MainActivity.this, "Puntuación guardada en Realtime Database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error
                        Toast.makeText(MainActivity.this, "Error al guardar la puntuación en Realtime Database", Toast.LENGTH_SHORT).show();
                    }
                });
    }





    protected void onDestroy() {
        super.onDestroy();
        // Alliberar recursos en la destrucció de l'activitat
        GestorReproductorMusica.alliberar();
    }
}
