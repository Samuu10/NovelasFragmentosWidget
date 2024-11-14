package com.example.novelasfragmentoswidget.ui.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.example.novelasfragmentoswidget.R;
import com.example.novelasfragmentoswidget.ui.Activities.MainActivity;

//Clase que representa el widget de la aplicación
public class NovelasFavoritasWidget extends AppWidgetProvider {

    //Metodo que se ejecuta al actualizar el widget
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        //Se recorre la lista de widgets
        for (int appWidgetId : appWidgetIds) {

            //Se crea un intent para abrir la aplicación al hacer clic en el botón del widget
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.widget_button_open_app, pendingIntent);

            //Se crea un intent para cargar las novelas favoritas
            Intent serviceIntent = new Intent(context, WidgetService.class);
            views.setRemoteAdapter(R.id.widget_listview, serviceIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        WidgetUpdateService.startActionUpdateWidget(context);
    }

    //Metodo que se ejecuta al recibir un intent
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            WidgetUpdateService.startActionUpdateWidget(context);
        }
    }
}