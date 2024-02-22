package projecte.puzle;

import android.graphics.Bitmap;
public class SplitImatge {

    // Mètode per dividir la imatge en parts basades en l'amplada i alcada de les cel·les
    public static Bitmap[][] divideImage(Bitmap imatge, int files, int columnes, int ampladaCella, int alcadaCella) {
        int ampladaTaula = columnes * ampladaCella;
        int alcadaTaula = files * alcadaCella;

        // Redimensiona la imatge per ajustar-se a la mida de la taula
        Bitmap imatgeRedimensionada = Bitmap.createScaledBitmap(imatge, ampladaTaula, alcadaTaula, true);

        int amplada = imatgeRedimensionada.getWidth();
        int alcada = imatgeRedimensionada.getHeight();

        Bitmap[][] parts = new Bitmap[files][columnes];

        for (int fila = 0; fila < files; fila++) {
            for (int columna = 0; columna < columnes; columna++) {
                // Calcula la posició inicial per a aquesta part
                int startX = columna * ampladaCella;
                int startY = fila * alcadaCella;

                // Crea la part de la imatge redimensionada
                parts[fila][columna] = Bitmap.createBitmap(imatgeRedimensionada, startX, startY, ampladaCella, alcadaCella);
            }
        }

        return parts;
    }
}

