package projecte.puzle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView selectedImageView = null;
    private Button botoAlternarMusica;
    private int movimientoCounter = 0;
    private TextView score;
    private int rows, cols; // Variables para filas y columnas
    private int[][] originalPositions; // Matriz para almacenar las posiciones originales de las celdas
    private int[][] currentPositions; // Matriz para almacenar las posiciones actuales de las celdas
    private Button btnVolverInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVolverInicio = findViewById(R.id.btnVolverInicio);
        btnVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PantallaInicial.class);
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        int dimensiones = intent.getIntExtra("dimensiones", 0);
        if (dimensiones == 0) {
            Toast.makeText(this, "Error: Dimensiones no válidas", Toast.LENGTH_SHORT).show();
            finish();
        }

        rows = cols = dimensiones;

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

        int screenWidth = 1080;
        int screenHeight = 1920;
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

        originalPositions = new int[rows][cols];
        currentPositions = new int[rows][cols];

        // Initialize original positions with sequential values
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                originalPositions[i][j] = i * cols + j;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int position = positions.get(positionIndex++);
                int row = position / cols;
                int col = position % cols;

                currentPositions[i][j] = position;

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
                                int temp = currentPositions[selectedRow][selectedCol];
                                currentPositions[selectedRow][selectedCol] = currentPositions[clickedRow][clickedCol];
                                currentPositions[clickedRow][clickedCol] = temp;
                                Bitmap tempBitmap = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();
                                selectedImageView.setImageBitmap(((BitmapDrawable) clickedImageView.getDrawable()).getBitmap());
                                clickedImageView.setImageBitmap(tempBitmap);
                                selectedImageView.clearColorFilter();
                                selectedImageView = null;
                                movimientoCounter++;
                                score.setText("Movimientos: " + movimientoCounter);
                                guardarPuntuacionEnSegundoPlano(movimientoCounter);

                                if (puzzleCompleted()) {
                                    showPuzzleCompletedMessage();
                                    Log.d("MainActivity", "¡Puzzle completado!");
                                } else {
                                    Log.d("MainActivity", "Movimiento: " + movimientoCounter + ", Puzzle no completado");
                                }
                            }
                        } else {
                            selectedImageView = clickedImageView;
                            ColorMatrix matrix = new ColorMatrix();
                            matrix.setSaturation(0);
                            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                            selectedImageView.setColorFilter(filter);
                        }
                    }
                });
                gridLayout.addView(frameLayout);
            }
        }
    }

    private boolean puzzleCompleted() {
        int correctCells = 0; // Counter for correct cells

        // Iterate through each cell of the puzzle
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Check if the current position matches the original position
                if (currentPositions[i][j] == originalPositions[i][j]) {
                    correctCells++; // Increment correct cell counter
                }
            }
        }

        // Log the number of cells in their correct place
        Log.d("MainActivity", "Cells in correct place: " + correctCells);

        // If all cells are in their correct place, puzzle is completed
        return correctCells == rows * cols;
    }

    private void showPuzzleCompletedMessage() {
        Toast.makeText(this, "¡Puzzle completado!", Toast.LENGTH_SHORT).show();
    }

    private void guardarPuntuacionEnSegundoPlano(int puntuacion) {
        Intent intent = new Intent(this, SaveScoreService.class);
        intent.putExtra("score", puntuacion);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GestorReproductorMusica.alliberar();
    }
}
