package com.example.novelasfragmentoswidget.ui.Fragmentos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.novelasfragmentoswidget.R;
import com.example.novelasfragmentoswidget.ui.GestionNovelas.Novela;
import com.example.novelasfragmentoswidget.ui.Almacenamiento.PreferencesManager;
import java.util.List;

public class DetallesNovelasFragment extends Fragment implements PreferencesManager.LoadNovelasCallback {

    private Novela novela;
    private PreferencesManager preferencesManager;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_novela, container, false);
        TextView textViewTitulo = view.findViewById(R.id.textViewTituloDetalle);
        TextView textViewAutor = view.findViewById(R.id.textViewAutorDetalle);
        TextView textViewAño = view.findViewById(R.id.textViewAñoDetalle);
        TextView textViewSinopsis = view.findViewById(R.id.textViewSinopsisDetalle);

        preferencesManager = new PreferencesManager(requireContext());

        if (getArguments() != null) {
            novela = getArguments().getParcelable("novela");
            if (novela != null) {
                textViewTitulo.setText(novela.getTitulo());
                textViewAutor.setText(novela.getAutor());
                textViewAño.setText(String.valueOf(novela.getAñoPublicacion()));
                textViewSinopsis.setText(novela.getSinopsis());
            }
        }

        view.findViewById(R.id.btn_favorito).setOnClickListener(v -> {
            novela.setFavorito(!novela.getFavorito());
            preferencesManager.loadNovelas(this);
        });

        view.findViewById(R.id.btn_volver).setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    @Override
    public void onNovelasLoaded(List<Novela> loadedNovelas) {
        for (Novela n : loadedNovelas) {
            if (n.equals(novela)) {
                n.setFavorito(novela.getFavorito());
                break;
            }
        }
        preferencesManager.saveNovelas(loadedNovelas);
        Toast.makeText(getContext(), novela.getFavorito() ? "Añadida a favoritos" : "Eliminada de favoritos", Toast.LENGTH_SHORT).show();
    }
}