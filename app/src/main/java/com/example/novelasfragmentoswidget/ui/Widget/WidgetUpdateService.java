package com.example.novelasfragmentoswidget.ui.Widget;

import android.content.Context;
import android.content.Intent;
import androidx.core.app.JobIntentService;

//Clase que representa el servicio de actualización del widget de la aplicación
public class WidgetUpdateService extends JobIntentService {

    //Variable que representa el identificador del trabajo en segundo plano
    private static final int JOB_ID = 1000;

    //Metodo para iniciar la acción de actualización del widget
    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        enqueueWork(context, WidgetUpdateService.class, JOB_ID, intent);
    }

    //Metodo que ejecuta el trabajo en segundo plano
    @Override
    protected void onHandleWork(Intent intent) {
        new WidgetUpdateTask(this).execute();
    }
}