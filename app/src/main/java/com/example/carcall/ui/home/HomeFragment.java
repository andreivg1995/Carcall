package com.example.carcall.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.carcall.MVVMRepository;
import com.example.carcall.R;
import com.example.carcall.model.Viaje;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;

    private GoogleMap mMap;

    SupportMapFragment mMapFragment;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_CODE = 101;

    private Marker marker = null;

    Location loc1, loc2;

    Button bReserva;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapa);
        mMapFragment.getMapAsync(this);

        bReserva = root.findViewById(R.id.buttonReserva);

        bReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Reservando...", Toast.LENGTH_SHORT).show();

                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                List<Address> addresses  = null;
                try {
                    addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String zip = addresses.get(0).getPostalCode();
                String country = addresses.get(0).getCountryName();

                // Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                //push() genera un id
                databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("viajes").push();

                databaseReference.setValue(new Viaje(databaseReference.getKey(), MVVMRepository.getDiaActual(MVVMRepository.fechaActualDiaHora()),
                        address, "", "", "", user.getUid()));
            }
        });

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    final MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    mMap.addMarker(markerOptions);

                    //Guardar localización
                    loc1 = new Location("");
                    loc1.setLatitude(latLng.latitude);
                    loc1.setLongitude(latLng.longitude);

                    //Destination with location click from map
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng point) {
                            if (marker != null) {
                                marker.remove();
                            }
                            loc2 = new Location("");
                            LatLng latLng2 = new LatLng(point.latitude, point.longitude);
                            marker = mMap.addMarker(new MarkerOptions().position(latLng2).title("Destination"));

                            loc2.setLatitude(latLng2.latitude);
                            loc2.setLongitude(latLng2.longitude);

                            //Se envia el origen y el destino al thread
                            CalculateDistance calculateDistance = new CalculateDistance(loc1, loc2);
                            calculateDistance.execute();

                            System.out.println(point.latitude + "---" + point.longitude);
                        }
                    });
                }
            }
        });
        // Marker in Sydney
        /*
        LatLng latLng = new LatLng(-34,151);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Sydney"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mMapFragment.getMapAsync(this);
                }
                break;
        }
    }

    public class CalculateDistance extends AsyncTask<String, String, String> {

        String forecastJsonStr = null;
        Location orig, dest;

        public CalculateDistance(Location orig, Location dest) {
            this.orig = orig;
            this.dest = dest;
        }

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader reader = null;
            HttpURLConnection urlConnection = null;
            URL url = null;

            LatLng latLng = new LatLng(orig.getLatitude(), orig.getLongitude());
            LatLng latLng2 = new LatLng(dest.getLatitude(), dest.getLongitude());

            try {
                url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins="+
                        latLng.latitude+","+latLng.longitude+"&destinations="+latLng2.latitude+","+latLng2.longitude+
                        "&mode=driving&key=AIzaSyA22u5QBvoTQ4bSYuotFcm_4EQIySHmWVA&sensor=true");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                forecastJsonStr = buffer.toString();

                Log.i("@@@@@@@@@@@@@@@@@@@", forecastJsonStr);
                return forecastJsonStr;

            } catch (IOException e) {
                Log.e("Exception", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Exception", "Error closing stream", e);
                    }
                }
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                // get JSONObject from JSON file
                JSONObject obj = new JSONObject(forecastJsonStr);
                String dest = obj.getJSONArray("origin_addresses").toString();
                String orig = obj.getJSONArray("destination_addresses").toString();

                String distance = obj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements")
                        .getJSONObject(0).getJSONObject("distance").get("text").toString();
                String duration = obj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements")
                        .getJSONObject(0).getJSONObject("duration").get("text").toString();

                Log.i("@@@@@@@@@@@@@@@@@@@@@@", orig);
                Log.i("@@@@@@@@@@@@@@@@@@@@@@", dest);
                Log.i("@@@@@@@@@@@@@@@@@@@@@@", distance);
                Log.i("@@@@@@@@@@@@@@@@@@@@@@", duration);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}