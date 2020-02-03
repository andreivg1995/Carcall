package com.example.carcall.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carcall.R;
import com.example.carcall.model.Viaje;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private RecyclerView recyclerView;
    private AdapterViajes adapter;

    FirebaseAuth mAuth;
    FirebaseRecyclerOptions<Viaje> options;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView = root.findViewById(R.id.recyclerMisViajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        galleryViewModel.firebaseQuery(user);

        galleryViewModel.getViajes().observe(this, new Observer<Query>() {
            @Override
            public void onChanged(Query query) {
                options =
                        new FirebaseRecyclerOptions.Builder<Viaje>()
                                .setQuery(query, Viaje.class)
                                .build();

                adapter = new AdapterViajes(options);
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }
        });
        return root;
    }

    /*
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    */

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
        protected void onBindViewHolder(@NonNull final AdapterViajes.ViewHolder holder, final int position, @NonNull final Viaje model) {
            // Bind the Chat object to the ChatHolder
            holder.tvFechaReserva.setText(model.getFecha());
            holder.tvCalleSalida.setText(model.getcSalida());
            holder.tvCalleLlegada.setText(model.getcLlegada());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Has pulsado " + model.getFecha(), Toast.LENGTH_SHORT).show();

                    ViajesDetailFragment fragment = new ViajesDetailFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("salida_key", model.gethSalida());
                    bundle.putString("llegada_key", model.gethLlegada());

                    fragment.setArguments(bundle);
                    //Se crea un nuevo fragment en mobile_navigation con el layout correspondiente de la clase detalle
                    Navigation.findNavController(holder.itemView).navigate(R.id.nav_viajes_detail, bundle);
                }
            });
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
            TextView tvFechaReserva, tvCalleSalida, tvCalleLlegada;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tvFechaReserva = itemView.findViewById(R.id.tvFechaReserva);
                tvCalleSalida = itemView.findViewById(R.id.tvCalleSalida);
                tvCalleLlegada = itemView.findViewById(R.id.tvCalleLlegada);
            }
        }
    }
}