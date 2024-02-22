package projecte.puzle;

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
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import projecte.puzle.R;
import projecte.puzle.SplitImatge;

public class MainActivity extends AppCompatActivity {
    private ImageView selectedImageView = null;
    private Button botoAlternarMusica;
    private int movimientoCounter = 0;
    private TextView score;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Dentro del método onCreate() de MainActivity
        DatabaseReference DBArtistes = FirebaseDatabase.getInstance().getReference("Pene");

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

        int rows = 3; // Set the number of rows in your grid
        int cols = 3; // Set the number of columns in your grid

        // Definir valores específicos para la anchura y altura de la pantalla
        int screenWidth = 1080; // Ancho de la pantalla en píxeles
        int screenHeight = 1920; // Alto de la pantalla en píxeles

        int cellWidth = screenWidth / cols;
        int cellHeight = screenHeight / rows;

        gridLayout.setColumnCount(cols);
        gridLayout.setRowCount(rows);

        Bitmap[][] parts = SplitImatge.divideImage(originalImage, rows, cols, cellWidth, cellHeight);

        // Generate a list of all possible positions
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < rows * cols; i++) {
            positions.add(i);
        }

        // Shuffle the list of positions
        Collections.shuffle(positions);

        // Counter for iterating through the shuffled positions
        int positionIndex = 0;

        // Iterate over each cell and assign a shuffled position
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Get the next shuffled position
                int position = positions.get(positionIndex++);
                int row = position / cols;
                int col = position % cols;

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(cellWidth, cellHeight));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                if (position == rows * cols - 1) {
                    // Si es la última posición, asignar un bitmap vacío
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

                            // Obtiene las coordenadas (fila, columna) de la celda seleccionada y la celda vacía
                            int selectedRow = selectedPosition / cols;
                            int selectedCol = selectedPosition % cols;
                            int clickedRow = clickedPosition / cols;
                            int clickedCol = clickedPosition % cols;


                            // Comprueba si la celda seleccionada está adyacente a la celda vacía
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

    protected void onDestroy() {
        super.onDestroy();
        // Alliberar recursos en la destrucció de l'activitat
        GestorReproductorMusica.alliberar();
    }
}
