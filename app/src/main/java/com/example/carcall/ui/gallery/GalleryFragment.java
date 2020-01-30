package com.example.carcall.ui.gallery;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carcall.R;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private RecyclerView recyclerView;
    private AdapterViajes adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView = root.findViewById(R.id.recyclerMisViajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public class AdapterViajes extends FirebaseRecyclerAdapter<Viaje, AdapterViajes.ViewHolder> {
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public AdapterViajes(@NonNull FirebaseRecyclerOptions<Viaje> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull AdapterViajes.ViewHolder holder, int position, @NonNull Viaje model) {
            // Bind the Chat object to the ChatHolder
            holder.tvCalleSalida.setText(model.getMensaje());
        }

        @NonNull
        @Override
        public AdapterViajes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Create a new instance of the ViewHolder, in this case we are using a custom
            // layout called R.layout.message for each item
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mis_viajes, parent,false);
            return new ViewHolder(view);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvCalleSalida;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tvCalleSalida = itemView.findViewById(R.id.tvCalleSalida);
            }
        }
    }
}