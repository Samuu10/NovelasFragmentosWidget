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

public class ListaNovelasFragment extends Fragment implements PreferencesManager.LoadNovelasCallback {

    private NovelaAdapter adapter;
    private RecyclerView recyclerView;
    private PreferencesManager preferencesManager;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_novelas, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewMainNovelas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        preferencesManager = new PreferencesManager(requireContext());
        preferencesManager.loadNovelas(this);
        return view;
    }

    @Override
    public void onNovelasLoaded(List<Novela> loadedNovelas) {
        adapter = new NovelaAdapter(getContext(), loadedNovelas, novela -> {
            // Handle item click
            ((MainActivity) getActivity()).mostrarDetallesNovela(novela);
        });
        recyclerView.setAdapter(adapter);
    }

    public void actualizarLista(List<Novela> nuevasNovelas) {
        if (adapter != null) {
            adapter.setNovelas(nuevasNovelas);
            adapter.notifyDataSetChanged();
        }
    }
}