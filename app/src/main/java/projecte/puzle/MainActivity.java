package projecte.puzle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap originalImage = BitmapFactory.decodeResource(getResources(), R.drawable.pb); // Replace `your_image` with the ID of your image
        Bitmap[][] parts = SplitImatge.divideImage(originalImage, 4, 4);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int cellWidth = (screenWidth - 16) / 4; // Calculate the width of each cell considering padding

        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int randomRow = random.nextInt(4); // Generate a random row index
                int randomCol = random.nextInt(4); // Generate a random column index

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(cellWidth, cellWidth)); // Set the size of the ImageView
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); // Scale the image to fit the ImageView
                imageView.setImageBitmap(parts[i][j]);

                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(cellWidth, cellWidth));
                frameLayout.setPadding(8, 8, 8, 8); // Set padding between cells (adjust as needed)

                frameLayout.addView(imageView);

                int childCount = gridLayout.getChildCount();
                int randomIndex = random.nextInt(childCount + 1); // Generate a random index within the range [0, childCount]

                gridLayout.addView(frameLayout, randomIndex);            }
        }
    }
}
