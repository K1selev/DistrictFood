package ru.techpark.districtfood.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.CachingByRoom.Restaurant;
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
    private static final int SET_INTERVAL_FOR_ACTIVE_LOCATION_UPDATES = 3;
    private boolean locationPermissionGranted;

    private Location lastKnownLocation;
    private CameraPosition cameraPosition;

    private static final String KEY_LOCATION = "location";
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private String name_clicked_marker;
    private Bundle arguments;
    private View mLayout;
    private TextView distance_text;
    private TextView distanceRouteRealTime;
    private ImageButton stopRoute;
    private View LayoutDistance;
    private Button go_route;
    private BottomSheetBehavior behavior;

    public FragmentMap() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Bundle arguments = getArguments();

        if (arguments != null) {
            ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mapViewModel.getSomething().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("test", s);
            }
        });

        Places.initialize(ApplicationModified.contextApplication, "${MAPS_API_KEY}");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        ApplicationModified.enabled_recyclerView = false;

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

        behavior = BottomSheetBehavior.from(view.findViewById(R.id.bottomsheet));
        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        go_route = view.findViewById(R.id.go_route);
        go_route.setOnClickListener(GO_ROUTE);

        distance_text = view.findViewById(R.id.distance);
        distanceRouteRealTime = view.findViewById(R.id.distanceRouteRealTime);
        stopRoute = view.findViewById(R.id.stop_route);
        stopRoute.setOnClickListener(ClickStopRoute);
        LayoutDistance = view.findViewById(R.id.distanceLayout);

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

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(SET_INTERVAL_FOR_ACTIVE_LOCATION_UPDATES); // 3 seconds interval
        mLocationRequest.setFastestInterval(SET_INTERVAL_FOR_ACTIVE_LOCATION_UPDATES);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(ApplicationModified.contextApplication,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        }
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
                getDeviceLocationOnStart();
            } else {
                map.setMyLocationEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocationOnStart() {
        /*try {
            if (locationPermissionGranted) {

                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
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
        }*/

        try {
            if (locationPermissionGranted) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    if (lastKnownLocation == null) {
                                        lastKnownLocation = location;
                                    }
                                    if (cameraPosition == null) {
                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(lastKnownLocation.getLatitude(),
                                                        lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                    } else {
                                        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    }

                                    com.google.maps.model.LatLng location123 = new com.google.maps.model.LatLng(
                                            lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                    ApplicationModified.myLocation = location123;

                                    GO_ACTION(location123);
                                } else {
                                    map.moveCamera(CameraUpdateFactory
                                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                                    map.getUiSettings().setMyLocationButtonEnabled(false);
                                }
                            }
                        });
            }
        } catch (SecurityException e) {
            //Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
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
    public void onPause() {
        super.onPause();
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (map != null) {
            ApplicationModified.bundle_for_maps.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            ApplicationModified.bundle_for_maps.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        ApplicationModified.enabled_recyclerView = true;
        ApplicationModified.updateRoute = false;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (ContextCompat.checkSelfPermission(ApplicationModified.contextApplication,
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
        //Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        ApplicationModified.marker = marker;
        String name_marker = marker.getTitle();
        // Check if a click count was set, then display the click count.
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        name_clicked_marker = name_marker;

        float[] result = new float[3];
        Location.distanceBetween(marker.getPosition().latitude, marker.getPosition().longitude,
                ApplicationModified.myLocation.lat, ApplicationModified.myLocation.lng, result);

       String go_route_string = getResources().getString(R.string.go_route) + " '" + name_marker + "'";
       String distance = getResources().getString(R.string.distance) + " " + Math.ceil(result[0]) + " " +
               getResources().getString(R.string.meter);

        go_route.setText(go_route_string);
        distance_text.setText(distance);

        return false;
    }

    private final View.OnClickListener GO_ROUTE = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (hasConnection(requireContext())) {

                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                ApplicationModified.updateCameraPositionForRoute = true;

                Restaurant restaurant = null;
                for (Restaurant restaurant_for : ApplicationModified.restaurantList) {
                    if (restaurant_for.getName().toLowerCase().equals(name_clicked_marker.toLowerCase())) {
                        restaurant = restaurant_for;
                        break;
                    }
                }

                if (MapAction.getInstance().polyline() != null) {
                    MapAction.getInstance().polyline().remove();
                }
                MapAction.getInstance().Router(Constants.ACTION_ROUTE_OF_RESTAURANT, restaurant,
                        map, ApplicationModified.myLocation);
            }
            else  Toast.makeText(requireContext(), R.string.internet_connection, Toast.LENGTH_LONG).show();
        }
    };

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);

                //Create instance for current user lat and lng
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                com.google.maps.model.LatLng locationMy = new
                        com.google.maps.model.LatLng(location.getLatitude(), location.getLongitude());

                float[] result1 = new float[3];
                Location.distanceBetween(ApplicationModified.myLocation.lat,
                        ApplicationModified.myLocation.lng, latLng.latitude, latLng.longitude, result1);

                if(result1[0] > 20) {
                    ApplicationModified.myLocation = locationMy;
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                }

                if (ApplicationModified.marker != null) {
                    float[] result = new float[3];
                    Location.distanceBetween(ApplicationModified.marker.getPosition().latitude,
                            ApplicationModified.marker.getPosition().longitude,
                            ApplicationModified.myLocation.lat, ApplicationModified.myLocation.lng, result);

                    String distance = getResources().getString(R.string.distance) + " " + Math.ceil(result[0]) + " " +
                            getResources().getString(R.string.meter);
                    distance_text.setText(distance);
                }

                if (ApplicationModified.updateRoute && result1[0] > 1){
                    if (MapAction.getInstance().polyline() != null) {
                        MapAction.getInstance().polyline().remove();
                    }
                    MapAction.getInstance().Router(Constants.ACTION_ROUTE_OF_RESTAURANT,
                            ApplicationModified.restaurantForUpdateRoute,
                            map, ApplicationModified.myLocation);
                }

                if (ApplicationModified.updateRoute){
                    LayoutDistance.setVisibility(View.VISIBLE);
                    float[] result = new float[3];
                    Location.distanceBetween(ApplicationModified.marker.getPosition().latitude,
                            ApplicationModified.marker.getPosition().longitude,
                            latLng.latitude, latLng.longitude, result);

                    String distance = getResources().getString(R.string.distanceRouteRealTime)
                            + " " + Math.ceil(result[0]) + " " +
                            getResources().getString(R.string.meter);
                    distanceRouteRealTime.setText(distance);
                    if (Math.ceil(result[0]) < 3){
                        ApplicationModified.updateRoute = false;
                        LayoutDistance.setVisibility(View.GONE);
                    }
                }

            }
        }
    };

    private final View.OnClickListener ClickStopRoute = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MapAction.getInstance().polyline().remove();
            ApplicationModified.updateRoute = false;
            LayoutDistance.setVisibility(View.GONE);
            MapAction.getInstance().Router(Constants.ACTION_RESTAURANT_AROUND, null, map, null);
        }
    };

    public boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNW = cm.getActiveNetworkInfo();
        return activeNW != null && activeNW.isConnected();
    }

    private void GO_ACTION(com.google.maps.model.LatLng myLocation) {
        if (arguments != null) {
            ApplicationModified.updateCameraPositionForRoute = true;
            int id = arguments.getInt(Constants.ID);
            Restaurant restaurant1 = null;
            if (ApplicationModified.restaurantList != null) {
                for (Restaurant restaurant: ApplicationModified.restaurantList) {
                    if (restaurant.getId() == id) {
                        restaurant1 = restaurant;
                    }
                }
            }
            MapAction.getInstance().Router(Constants.ACTION_ROUTE_OF_RESTAURANT, restaurant1, map, myLocation);
        } else {
            MapAction.getInstance().Router(Constants.ACTION_RESTAURANT_AROUND, null, map, null);
        }
    }

}
