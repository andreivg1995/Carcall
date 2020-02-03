package com.example.carcall.ui.gallery;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carcall.R;

public class ViajesDetailFragment extends Fragment {

    private ViajesDetailViewModel viajesDetailViewModel;
    TextView hSalida, hLlegada;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //viajesDetailViewModel =
        View root = inflater.inflate(R.layout.fragment_viajes_detail, container, false);

        hSalida = root.findViewById(R.id.tvhSalida);
        hLlegada = root.findViewById(R.id.tvhLlegada);

        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        String bhSalida = bundle.getString("salida_key");
        String bhLlegada = bundle.getString("llegada_key");

        hSalida.setText(bhSalida);
        hLlegada.setText(bhLlegada);

        return root;
    }
}
