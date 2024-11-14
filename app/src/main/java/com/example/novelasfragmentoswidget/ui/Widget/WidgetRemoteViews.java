package com.example.novelasfragmentoswidget.ui.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.novelasfragmentoswidget.R;
import com.example.novelasfragmentoswidget.ui.Almacenamiento.PreferencesManager;
import com.example.novelasfragmentoswidget.ui.GestionNovelas.Novela;
import java.util.ArrayList;
import java.util.List;

//Clase que representa la factoria de vistas remotas del widget de la aplicación
public class WidgetRemoteViews implements RemoteViewsService.RemoteViewsFactory {

    //Variables
    private Context context;
    private List<Novela> favoritas;

    //Constructor
    public WidgetRemoteViews(Context context) {
        this.context = context;
    }

    //Metodo que inicializa el widget
    @Override
    public void onCreate() {}

    //Metodo que se ejecuta al cambiar los datos del widget
    @Override
    public void onDataSetChanged() {

        //Se obtienen las novelas favoritas del usuario
        PreferencesManager preferencesManager = new PreferencesManager(context);
        favoritas = preferencesManager.loadNovelasSync();

        //Si la lista de novelas favoritas no es nula, se eliminan las novelas que no son favoritas
        if (favoritas != null) {
            favoritas.removeIf(novela -> !novela.getFavorito());
        } else {
            favoritas = new ArrayList<>();
        }
    }

    //Metodo que se ejecuta al destruir el widget
    @Override
    public void onDestroy() {
        if (favoritas != null) {
            favoritas.clear();
        }
    }

    //Metodo que devuelve el numero de elementos de la lista de novelas favoritas
    @Override
    public int getCount() {
        return favoritas != null ? favoritas.size() : 0;
    }

    //Metodo que crea las vistas remotas del widget
    @Override
    public RemoteViews getViewAt(int position) {

        //Si la lista de novelas favoritas es nula o está vacía, se devuelve nulo
        if (favoritas == null || favoritas.size() == 0) {
            return null;
        }

        //Se obtiene la novela de la posición actual
        Novela novela = favoritas.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_novela_item);
        views.setTextViewText(R.id.textViewTitulo, novela.getTitulo());

        //Se crea un intent para abrir la aplicación al hacer clic en la novela
        Intent fillInIntent = new Intent();
        views.setOnClickFillInIntent(R.id.item_novela_layout, fillInIntent);

        return views;
    }

    //Metodo que devuelve el tipo de vista
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    //Metodo que devuelve el numero de tipos de vistas
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    //Metodo que devuelve el id del elemento
    @Override
    public long getItemId(int position) {
        return position;
    }

    //Metodo que devuelve si los ids son estables
    @Override
    public boolean hasStableIds() {
        return true;
    }
}