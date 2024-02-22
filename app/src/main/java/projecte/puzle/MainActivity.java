package projecte.puzle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import projecte.puzle.R;
import projecte.puzle.SplitImatge;

public class MainActivity extends AppCompatActivity {
    private ImageView selectedImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap originalImage = BitmapFactory.decodeResource(getResources(), R.drawable.pb);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        int rows = 2; // Set the number of rows in your grid
        int cols = 2; // Set the number of columns in your grid

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

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
                imageView.setImageBitmap(parts[row][col]);

                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(cellWidth, cellHeight));
                frameLayout.setPadding(4, 4, 4, 4);
                frameLayout.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedImageView == null) {
                            selectedImageView = (ImageView) v;

                            ColorMatrix matrix = new ColorMatrix();
                            matrix.setSaturation(0);

                            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                            selectedImageView.setColorFilter(filter);       // Cambiar color al clickar

                        } else {
                            Bitmap tempBitmap = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();
                            selectedImageView.setImageBitmap(((BitmapDrawable) ((ImageView) v).getDrawable()).getBitmap());
                            ((ImageView) v).setImageBitmap(tempBitmap);
                            selectedImageView.clearColorFilter();
                            selectedImageView = null;
                        }
                    }
                });

                gridLayout.addView(frameLayout);
            }
        }
    }
}
