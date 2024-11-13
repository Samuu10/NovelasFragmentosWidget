package com.example.novelasfragmentoswidget.ui.Fragmentos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.novelasfragmentoswidget.R;
import com.example.novelasfragmentoswidget.ui.Activities.MainActivity;
import com.example.novelasfragmentoswidget.ui.Almacenamiento.PreferencesManager;
import com.example.novelasfragmentoswidget.ui.GestionNovelas.Novela;
import com.example.novelasfragmentoswidget.ui.GestionNovelas.NovelaAdapter;
import java.util.List;
import java.util.stream.Collectors;

//Clase ListaFavoritasFragment que representa el fragmento que muestra la lista de novelas favoritas
public class ListaFavoritasFragment extends Fragment implements PreferencesManager.LoadNovelasCallback {

    //Variables
    private NovelaAdapter adapter;
    private RecyclerView recyclerView;
    private PreferencesManager preferencesManager;

    //Metodo para crear la vista del fragmento
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_lista_favoritas, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewFavoritasNovelas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        preferencesManager = new PreferencesManager(requireContext());
        preferencesManager.loadNovelas(this);
        return view;
    }

    //Metodo para gestionar los detalles de las novelas favoritas cargadas
    @Override
    public void onNovelasLoaded(List<Novela> loadedNovelas) {
        List<Novela> favoritas = loadedNovelas.stream()
                .filter(Novela::getFavorito)
                .collect(Collectors.toList());
        adapter = new NovelaAdapter(getContext(), favoritas, novela -> {
            ((MainActivity) getActivity()).mostrarDetallesNovela(novela);
        });
        recyclerView.setAdapter(adapter);
    }

    //Metodo para actualizar la lista de novelas favoritas
    public void actualizarLista(List<Novela> nuevasNovelas) {
        List<Novela> favoritas = nuevasNovelas.stream()
                .filter(Novela::getFavorito)
                .collect(Collectors.toList());
        adapter.setNovelas(favoritas);
        adapter.notifyDataSetChanged();
    }
}