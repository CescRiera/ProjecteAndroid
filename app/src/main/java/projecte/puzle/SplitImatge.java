package projecte.puzle;

import android.graphics.Bitmap;

public class SplitImatge {

    // Method to divide the image into specified number of rows and columns
    public static Bitmap[][] divideImage(Bitmap image, int rows, int cols) {
        int width = image.getWidth();
        int height = image.getHeight();
        int partWidth = width / cols;
        int partHeight = height / rows;

        Bitmap[][] parts = new Bitmap[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                parts[row][col] = Bitmap.createBitmap(image, col * partWidth, row * partHeight, partWidth, partHeight);
            }
        }

        return parts;
    }
}
