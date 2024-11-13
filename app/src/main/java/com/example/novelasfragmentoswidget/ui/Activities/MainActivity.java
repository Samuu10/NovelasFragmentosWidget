package com.example.novelasfragmentoswidget.ui.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.novelasfragmentoswidget.ui.GestionNovelas.NovelaAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PreferencesManager.LoadNovelasCallback {

    private List<Novela> novelas;
    private NovelaAdapter adapter;
    private FirebaseHelper firebaseHelper;
    private PreferencesManager preferencesManager;
    private boolean showingFavorites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferencesManager = new PreferencesManager(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseHelper = new FirebaseHelper();
        preferencesManager.loadNovelas(this);

        // Set up buttons
        findViewById(R.id.btn_agregar).setOnClickListener(v -> mostrarDialogoAñadirNovela());
        findViewById(R.id.btn_eliminar).setOnClickListener(v -> mostrarDialogoEliminarNovela());
        findViewById(R.id.btn_cambiar_lista).setOnClickListener(v -> cambiarLista());

        // Load the main list fragment at startup
        loadFragment(new ListaNovelasFragment(), "Lista de Novelas");
    }

    @Override
    public void onNovelasLoaded(List<Novela> loadedNovelas) {
        novelas = loadedNovelas;
        actualizarFragmentos();
    }

    // Method to load a fragment with a title
    private void loadFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        setTitle(title);
    }

    // Method to switch between the main list and favorites list
    private void cambiarLista() {
        if (showingFavorites) {
            loadFragment(new ListaNovelasFragment(), "Lista de Novelas");
        } else {
            loadFragment(new ListaFavoritasFragment(), "Lista de Favoritas");
        }
        showingFavorites = !showingFavorites;
    }

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

    private void añadirNovelaLista(String titulo) {
        for (Novela novela : novelas) {
            if (novela.getTitulo().equalsIgnoreCase(titulo)) {
                Toast.makeText(MainActivity.this, "La novela ya está en la lista", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        firebaseHelper.cargarNovelas(titulo, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot novelaSnapshot : snapshot.getChildren()) {
                        Novela novela = novelaSnapshot.getValue(Novela.class);
                        if (novela != null) {
                            novelas.add(novela);
                            preferencesManager.saveNovelas(novelas);
                            actualizarFragmentos();
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

    private void eliminarNovelaLista(String titulo) {
        for (Novela novela : novelas) {
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

    private void actualizarFragmentos() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof ListaNovelasFragment) {
            ((ListaNovelasFragment) fragment).actualizarLista(novelas);
        } else if (fragment instanceof ListaFavoritasFragment) {
            ((ListaFavoritasFragment) fragment).actualizarLista(novelas);
        }
    }

    public void mostrarDetallesNovela(Novela novela) {
        DetallesNovelasFragment fragment = new DetallesNovelasFragment();
        Bundle args = new Bundle();
        args.putParcelable("novela", novela);
        fragment.setArguments(args);
        loadFragment(fragment, "Detalles de la Novela");
    }
}