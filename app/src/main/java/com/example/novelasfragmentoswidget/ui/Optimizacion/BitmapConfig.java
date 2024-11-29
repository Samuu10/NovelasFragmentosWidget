package com.example.novelasfragmentoswidget.ui.Optimizacion;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//Clase para configurar y decodificar Bitmaps de manera eficiente
public class BitmapConfig {

    //Metodo para decodificar un Bitmap de recursos con un tamaño específico
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        //Crear¡mos opciones de BitmapFactory y establecemos inJustDecodeBounds a true para obtener las dimensiones del Bitmap
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //Decodificamos el recurso para obtener las dimensiones del Bitmap
        BitmapFactory.decodeResource(res, resId, options);

        //Calculamos el tamaño de muestra adecuado
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //Establecemos inJustDecodeBounds a false para decodificar el Bitmap real
        options.inJustDecodeBounds = false;
        // Decodificamos el recurso con el tamaño de muestra calculado
        return BitmapFactory.decodeResource(res, resId, options);
    }

    //Metodo para calcular el tamaño de muestra adecuado
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //Obtenemos las dimensiones originales del Bitmap
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //Si las dimensiones originales son mayores que las requeridas, calculmos el tamaño de muestra
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            //Calculamos el tamaño de muestra más grande que sea una potencia de 2 y mantenga ambas dimensiones mayores que las requeridas
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}