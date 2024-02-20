package projecte.puzle;

import android.graphics.Bitmap;

public class SplitImatge {

    // Method to divide the image into parts based on cell width and height
    public static Bitmap[][] divideImage(Bitmap image, int rows, int cols, int cellWidth, int cellHeight) {
        int tableWidth = cols * cellWidth;
        int tableHeight = rows * cellHeight;

        // Resize the image to fit the table size
        Bitmap resizedImage = Bitmap.createScaledBitmap(image, tableWidth, tableHeight, true);

        int width = resizedImage.getWidth();
        int height = resizedImage.getHeight();

        Bitmap[][] parts = new Bitmap[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Calculate the starting position for this part
                int startX = col * cellWidth;
                int startY = row * cellHeight;

                // Create the part of the resized image
                parts[row][col] = Bitmap.createBitmap(resizedImage, startX, startY, cellWidth, cellHeight);
            }
        }

        return parts;
    }
}
