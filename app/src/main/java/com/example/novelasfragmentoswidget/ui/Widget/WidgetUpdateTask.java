package com.example.novelasfragmentoswidget.ui.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import com.example.novelasfragmentoswidget.R;
import com.example.novelasfragmentoswidget.ui.Activities.MainActivity;
import com.example.novelasfragmentoswidget.ui.Almacenamiento.PreferencesManager;
import com.example.novelasfragmentoswidget.ui.GestionNovelas.Novela;

import java.util.ArrayList;
import java.util.List;

//Clase que gestiona la actualización del widget de la aplicación en segundo plano
public class WidgetUpdateTask extends AsyncTask<Void, Void, List<Novela>> {

    //Variable
    private Context context;

    //Constructor
    public WidgetUpdateTask(Context context) {
        this.context = context;
    }

    //Metodo que se ejecuta en segundo plano y carga las novelas favoritas del usuario
    @Override
    protected List<Novela> doInBackground(Void... voids) {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        List<Novela> favoritas = preferencesManager.loadNovelasSync();
        if (favoritas != null) {
            favoritas.removeIf(novela -> !novela.getFavorito());
        } else {
            favoritas = new ArrayList<>();
        }
        return favoritas;
    }

    //Metodo que actualiza el widget de la aplicación
    @Override
    protected void onPostExecute(List<Novela> favoritas) {

        //Si la lista de novelas favoritas es nula, se crea una nueva lista
        if (favoritas == null) {
            favoritas = new ArrayList<>();
        }

        //Se obtiene el widget manager y las vistas del widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        //Se establece el título del widget y se crea un intent para cargar las novelas favoritas
        views.setTextViewText(R.id.widget_title, "NOVELAS FAVORITAS: ");
        Intent intent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_listview, intent);

        //Se crea un intent para abrir la aplicación al hacer clic en el botón del widget
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_button_open_app, pendingIntent);

        //Se actualiza el widget
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, NovelasFavoritasWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }
}