package ru.techpark.districtfood.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.snackbar.Snackbar;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.MainActivity;
import ru.techpark.districtfood.R;


public class FragmentMap extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener{

    private MapViewModel mapViewModel;
    private GoogleMap map;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 17;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    private Location lastKnownLocation;
    private CameraPosition cameraPosition;

    private static final String KEY_LOCATION = "location";
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private Bundle arguments;
    private View mLayout;

    public FragmentMap() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();

        if (arguments != null) {
            ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapViewModel.getSomething().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("test", s);
            }
        });

        Places.initialize(ApplicationModified.context123, "${MAPS_API_KEY}");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arguments = getArguments();
        mLayout = view.findViewById(R.id.main_layout);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        } else if (ApplicationModified.bundle_for_maps.getParcelable(KEY_LOCATION) != null) {
            lastKnownLocation = ApplicationModified.bundle_for_maps.getParcelable(KEY_LOCATION);
            cameraPosition = ApplicationModified.bundle_for_maps.getParcelable(KEY_CAMERA_POSITION);
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

            }
        });

    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(ApplicationModified.context123,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } /*else if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            PermissionUtils.RationaleDialog.newInstance(PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, false)
                    .show(getChildFragmentManager(), "dialog");
        }*/
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.setOnMarkerClickListener(this);
                getDeviceLocation();
            } else {
                map.setMyLocationEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener( (MainActivity) requireActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.

                            if (lastKnownLocation == null) {
                                lastKnownLocation = task.getResult();
                            }
                            if (lastKnownLocation != null && cameraPosition == null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }

                            com.google.maps.model.LatLng location = new com.google.maps.model.LatLng(
                                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                            ApplicationModified.myLocation = location;

                            GO_ACTION(location);
                        } else {
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                locationPermissionGranted = false;
                if (isGranted) {
                    locationPermissionGranted = true;
                } else {
                    Snackbar.make(mLayout, R.string.permission_rationale_location_from_map,
                            Snackbar.LENGTH_LONG)
                            .show();
                }
                updateLocationUI();
            });


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (map != null) {
            ApplicationModified.bundle_for_maps.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            ApplicationModified.bundle_for_maps.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (ContextCompat.checkSelfPermission(ApplicationModified.context123,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.RationaleDialog.newInstance(PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, false)
                    .show(getChildFragmentManager(), "dialog");
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String name_marker = marker.getTitle();
        // Check if a click count was set, then display the click count.
        return false;
    }

    private void GO_ACTION(com.google.maps.model.LatLng myLocation) {
        if (arguments != null) {
            int id = arguments.getInt(Constants.ID);
            MapAction.getInstance().Router(Constants.ACTION_ROUTE_OF_RESTAURANT, id, map, myLocation);
        } else {
            MapAction.getInstance().Router(Constants.ACTION_RESTAURANT_AROUND, -1, map, null);
        }
    }


}
