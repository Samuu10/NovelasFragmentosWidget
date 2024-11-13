package com.example.novelasfragmentoswidget.ui.Almacenamiento;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.example.novelasfragmentoswidget.ui.GestionNovelas.Novela;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//Clase que se encarga de gestionar las preferencias de la aplicación del usuario
public class PreferencesManager {

    //Variables
    private static final String PREF_NAME = "user_preferences";
    private static final String KEY_NOVELAS = "novelas";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    //Constructor
    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    //Metodo para guardar las novelas
    public void saveNovelas(List<Novela> novelas) {
        new SaveNovelasTask().execute(novelas);
    }

    //Metodo para cargar las novelas
    public void loadNovelas(LoadNovelasCallback callback) {
        new LoadNovelasTask(callback).execute();
    }

    //Clase interna SaveNovelasTask que extiende AsyncTask y guarda las novelas en segundo plano
    private class SaveNovelasTask extends AsyncTask<List<Novela>, Void, Void> {
        @Override
        protected Void doInBackground(List<Novela>... lists) {
            List<Novela> novelas = lists[0];
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String json = gson.toJson(novelas);
            editor.putString(KEY_NOVELAS, json);
            editor.apply();
            return null;
        }
    }

    //Clase interna LoadNovelasTask que extiende AsyncTask y carga las novelas en segundo plano
    private class LoadNovelasTask extends AsyncTask<Void, Void, List<Novela>> {
        private LoadNovelasCallback callback;

        public LoadNovelasTask(LoadNovelasCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<Novela> doInBackground(Void... voids) {
            String json = sharedPreferences.getString(KEY_NOVELAS, null);
            Type type = new TypeToken<ArrayList<Novela>>() {}.getType();
            return json != null ? gson.fromJson(json, type) : new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Novela> novelas) {
            callback.onNovelasLoaded(novelas);
        }
    }

    //Interfaz LoadNovelasCallback para manejar la carga de novelas
    public interface LoadNovelasCallback {
        void onNovelasLoaded(List<Novela> novelas);
    }
}