package com.example.novelasfragmentoswidget.ui.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.novelasfragmentoswidget.R;
import com.example.novelasfragmentoswidget.ui.Almacenamiento.FirebaseHelper;
import com.example.novelasfragmentoswidget.ui.Almacenamiento.PreferencesManager;
import com.example.novelasfragmentoswidget.ui.Fragmentos.DetallesNovelasFragment;
import com.example.novelasfragmentoswidget.ui.Fragmentos.ListaFavoritasFragment;
import com.example.novelasfragmentoswidget.ui.Fragmentos.ListaNovelasFragment;
import com.example.novelasfragmentoswidget.ui.GestionNovelas.Novela;
import com.example.novelasfragmentoswidget.ui.Optimizacion.BrilloReceiver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

//Clase MainActivity que representa la actividad principal de la aplicacion
public class MainActivity extends AppCompatActivity implements PreferencesManager.LoadNovelasCallback {

    //Variables
    private List<Novela> novelas;
    private FirebaseHelper firebaseHelper;
    private PreferencesManager preferencesManager;
    private boolean showingFavorites = false;
    private BrilloReceiver brilloReceiver;
    private static final int REQUEST_CODE_WRITE_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Instanciar el PreferencesManager
        preferencesManager = new PreferencesManager(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instanciar el FirebaseHelper y cargar las novelas
        firebaseHelper = new FirebaseHelper();
        preferencesManager.loadNovelas(this);

        //Inicializar los botones de la actividad
        findViewById(R.id.btn_agregar).setOnClickListener(v -> mostrarDialogoAñadirNovela());
        findViewById(R.id.btn_eliminar).setOnClickListener(v -> mostrarDialogoEliminarNovela());
        findViewById(R.id.btn_cambiar_lista).setOnClickListener(v -> cambiarLista());

        //Cargamos el fragmento de la lista de novelas al iniciar la actividad
        loadFragment(new ListaNovelasFragment(), "Lista de Novelas");

        //Solicitamos el permiso para modificar los ajustes del sistema
        if (!Settings.System.canWrite(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
        } else {
            //Registramos el BroadcastReceiver
            registerBrilloReceiver();
        }
    }

    //Metodo para gestionar la navegacion hacia atras
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    //Metodo para gestionar la carga de las novelas
    @Override
    public void onNovelasLoaded(List<Novela> loadedNovelas) {
        novelas = loadedNovelas;
        actualizarFragmentos();
    }

    //Metodo para cargar un fragmento en el contenedor de fragmentos
    private void loadFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        setTitle(title);
    }

    //metodo para cambiar entre la lista de novelas y la lista de favoritas
    private void cambiarLista() {
        if (showingFavorites) {
            loadFragment(new ListaNovelasFragment(), "Lista de Novelas");
        } else {
            loadFragment(new ListaFavoritasFragment(), "Lista de Favoritas");
        }
        showingFavorites = !showingFavorites;
    }

    //Metodo para mostrar un dialogo para añadir una novela a la lista
    private void mostrarDialogoAñadirNovela() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.agregar_novela, null);
        EditText editTextTitle = view.findViewById(R.id.editTextTituloAñadir);

        builder.setView(view)
                .setTitle("Añadir Novela a la Lista")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Añadir", (dialog, id) -> {
                    String title = editTextTitle.getText().toString();
                    añadirNovelaLista(title);
                });
        builder.create().show();
    }

    //Metodo para mostrar un dialogo para eliminar una novela de la lista
    private void mostrarDialogoEliminarNovela() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.eliminar_novela, null);
        EditText editTextTitle = view.findViewById(R.id.editTextTituloEliminar);

        builder.setView(view)
                .setTitle("Eliminar Novela de la Lista")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Eliminar", (dialog, id) -> {
                    String title = editTextTitle.getText().toString();
                    eliminarNovelaLista(title);
                });
        builder.create().show();
    }

    //Metodo para añadir una novela a la lista
    private void añadirNovelaLista(String titulo) {

        //Bucle para comprobar si la novela ya está en la lista
        for (Novela novela : novelas) {
            if (novela.getTitulo().equalsIgnoreCase(titulo)) {
                Toast.makeText(MainActivity.this, "La novela ya está en la lista", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Si la novela no está en la lista, se carga desde Firebase y se añade a la lista
        firebaseHelper.cargarNovelas(titulo, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot novelaSnapshot : snapshot.getChildren()) {
                        Novela novela = novelaSnapshot.getValue(Novela.class);
                        if (novela != null) {
                            novela.setFavorito(false);
                            novelas.add(novela);
                            preferencesManager.saveNovelas(novelas);
                            actualizarFragmentos();
                            Toast.makeText(MainActivity.this, "Novela añadida a la lista", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Novela no encontrada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error al buscar novela: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Metodo para eliminar una novela de la lista
    private void eliminarNovelaLista(String titulo) {

        //Bucle para buscar la novela en la lista y eliminarla
        for (Novela novela : novelas) {
            //Si la novela se encuentra en la lista, se elimina y se actualizan las listas
            if (novela.getTitulo().equalsIgnoreCase(titulo)) {
                novelas.remove(novela);
                preferencesManager.saveNovelas(novelas);
                actualizarFragmentos();
                Toast.makeText(MainActivity.this, "Novela eliminada de la lista", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(MainActivity.this, "Novela no encontrada en la lista", Toast.LENGTH_SHORT).show();
    }

    //Metodo para actualizar los fragmentos
    private void actualizarFragmentos() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof ListaNovelasFragment) {
            ((ListaNovelasFragment) fragment).actualizarLista(novelas);
        } else if (fragment instanceof ListaFavoritasFragment) {
            ((ListaFavoritasFragment) fragment).actualizarLista(novelas);
        }
    }

    //Metodo para mostrar los detalles de una novela al hacer clic en ella
    public void mostrarDetallesNovela(Novela novela) {
        DetallesNovelasFragment fragment = new DetallesNovelasFragment();
        Bundle args = new Bundle();
        args.putParcelable("novela", novela);
        fragment.setArguments(args);
        loadFragment(fragment, "Detalles de la Novela");
    }

    //Metodo para gestionar la respuesta de la solicitud de permisos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Settings.System.canWrite(this)) {
                //Permiso concedido
                Toast.makeText(this, "Permiso concedido para modificar los ajustes del sistema", Toast.LENGTH_SHORT).show();
                // Registrar el BroadcastReceiver
                registerBrilloReceiver();
            } else {
                //Permiso denegado
                Toast.makeText(this, "Permiso denegado para modificar los ajustes del sistema", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Metodo para registrar el BroadcastReceiver
    private void registerBrilloReceiver() {
        brilloReceiver = new BrilloReceiver();
        registerReceiver(brilloReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    //Metodo para gestionar la pausa de la actividad y liberar recursos para optimizar la memoria
    @Override
    protected void onStop() {
        super.onStop();
        //Liberar referencias a vistas o adaptadores
        findViewById(R.id.btn_agregar).setOnClickListener(null);
        findViewById(R.id.btn_eliminar).setOnClickListener(null);
        findViewById(R.id.btn_cambiar_lista).setOnClickListener(null);
    }

    //Metodo para gestionar la destrucción de la actividad y liberar recursos para optimizar la memoria
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Liberar referencias a objetos grandes o contextos
        firebaseHelper = null;
        preferencesManager = null;
        novelas = null;

        //Desregistrar el BroadcastReceiver
        if(brilloReceiver != null) {
            unregisterReceiver(brilloReceiver);
        }
    }
}