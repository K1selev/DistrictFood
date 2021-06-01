package ru.techpark.districtfood.Map;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.List;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.CachingByRoom.Restaurant;
import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.R;

public class MapAction {

    private GoogleMap googleMap;
    private Polyline polyline;

    public void Router(String ACTION, Restaurant restaurant, GoogleMap map, LatLng myLocation) {
        googleMap = map;
        switch (ACTION) {
            case Constants.ACTION_RESTAURANT_AROUND:
                RestaurantAround();
                break;
            case Constants.ACTION_ROUTE_OF_RESTAURANT:
                RouteOfRestaurant(restaurant, myLocation);
                break;
        }
    }

    public void RestaurantAround() {

        if (ApplicationModified.restaurantList != null) {
            for (Restaurant restaurant: ApplicationModified.restaurantList) {

                LatLng place = new LatLng(restaurant.getX_coordinate(), restaurant.getY_coordinate());
                googleMap.addMarker(new MarkerOptions()
                        .position(new com.google.android.gms.maps.model.LatLng(place.lat, place.lng))
                        .title(restaurant.getName())).setTitle(restaurant.getName());

            }
        }

    }

    public void RouteOfRestaurant(Restaurant restaurant, LatLng myLocation) {

        ApplicationModified.restaurantForUpdateRoute = restaurant;
        ApplicationModified.updateRoute =true;

        LatLng place = new LatLng(restaurant.getX_coordinate(), restaurant.getY_coordinate());
        googleMap.addMarker(new MarkerOptions()
                .position(new com.google.android.gms.maps.model.LatLng(place.lat, place.lng))
                .title(restaurant.getName())).setTitle(restaurant.getName());

        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyBUzzbsW2Zxqfy-QeB-l8I2MCKsAh08RVQ")
                .build();
        DirectionsResult result = null;
        try {
            result = DirectionsApi.newRequest(geoApiContext)
                    .mode(TravelMode.WALKING)
                    .origin(place)
                    .destination(myLocation).await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<com.google.maps.model.LatLng> path = result.routes[0].overviewPolyline.decodePath();
        PolylineOptions line = new PolylineOptions();


        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        for (int i = 0; i < path.size(); i++) {
            line.add(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
            latLngBuilder.include(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
        }

        line.width(16f).color(R.color.colorPrimary);

        this.polyline = googleMap.addPolyline(line);

//Выставляем камеру на нужную нам позицию
        if (ApplicationModified.updateCameraPositionForRoute) {
            ApplicationModified.updateCameraPositionForRoute = false;
            LatLngBounds latLngBounds = latLngBuilder.build();
            int width = ApplicationModified.contextApplication.getResources().getDisplayMetrics().widthPixels;
            CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, width, width, 25);//width это размер нашего экрана
            googleMap.moveCamera(track);
        }

    }

    public Polyline polyline(){

        return this.polyline;

    }

    public static MapAction sInstance;
    public MapAction() {
    }
    public synchronized static MapAction getInstance(){
        if (sInstance == null) {
            sInstance = new MapAction();
        }
        return sInstance;
    }
}
