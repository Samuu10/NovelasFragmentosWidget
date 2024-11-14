package com.example.novelasfragmentoswidget.ui.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

//Clase que representa el servicio del widget
public class WidgetService extends RemoteViewsService {

    //Metodo que crea la factoria de vistas remotas
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViews(this.getApplicationContext());
    }
}