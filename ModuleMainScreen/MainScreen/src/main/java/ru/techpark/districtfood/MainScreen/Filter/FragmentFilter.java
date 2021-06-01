package ru.techpark.districtfood.MainScreen.Filter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.snackbar.Snackbar;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.MainScreen.CardsPreview.CardsAdapter;
import ru.techpark.districtfood.MainScreen.Search.Search;
import ru.techpark.districtfood.R;

public class FragmentFilter extends Fragment{

    private EditText filter_location_max;
    private EditText filter_middle_receipt;
    private Button filter_clear_button;
    private Button filter_apply_button;
    private ImageView score_filter_star1;
    private ImageView score_filter_star2;
    private ImageView score_filter_star3;
    private ImageView score_filter_star4;
    private ImageView score_filter_star5;
    private CheckBox filter_with_itself;
    private CheckBox filter_fast_food;
    private CheckBox filter_sale;
    public float number_star = 0;
    public String text_middle_receipt = "";
    public String text_location_max = "";
    public boolean tag_with_itself = false;
    public boolean tag_fast_food = false;
    public boolean tag_sale = false;
    private View layout;

    private boolean locationPermissionGranted;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint({"CutPasteId", "ClickableViewAccessibility"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout = view.findViewById(R.id.main_layout_filter);

        filter_location_max = view.findViewById(R.id.location_filter_max);
        filter_location_max.setOnEditorActionListener(Filter_location_max);
        filter_location_max.setOnTouchListener(Click_Filter_location_max);


        filter_middle_receipt = view.findViewById(R.id.middle_receipt_filter);
        filter_middle_receipt.setOnEditorActionListener(Filter_middle_receipt);

        score_filter_star1 = view.findViewById(R.id.icon_star_1);
        score_filter_star2 = view.findViewById(R.id.icon_star_2);
        score_filter_star3 = view.findViewById(R.id.icon_star_3);
        score_filter_star4 = view.findViewById(R.id.icon_star_4);
        score_filter_star5 = view.findViewById(R.id.icon_star_5);

        score_filter_star1.setOnClickListener(Filter_score);
        score_filter_star2.setOnClickListener(Filter_score);
        score_filter_star3.setOnClickListener(Filter_score);
        score_filter_star4.setOnClickListener(Filter_score);
        score_filter_star5.setOnClickListener(Filter_score);

        filter_with_itself = view.findViewById(R.id.with_itself);
        filter_fast_food = view.findViewById(R.id.fast_food);
        filter_sale = view.findViewById(R.id.sale);

        filter_clear_button = view.findViewById(R.id.clean_filter);
        filter_clear_button.setOnClickListener(ClearButton);

        filter_apply_button = view.findViewById(R.id.apply_filter);
        filter_apply_button.setOnClickListener(ApplyFilterButton);
    }

    private final  View.OnClickListener ApplyFilterButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ApplicationModified.bundleFilter.putBoolean(Constants.TAG_WITH_ITSELF, filter_with_itself.isChecked());
            ApplicationModified.bundleFilter.putBoolean(Constants.TAG_FAST_FOOD, filter_fast_food.isChecked());
            ApplicationModified.bundleFilter.putBoolean(Constants.TAF_SALE, filter_sale.isChecked());
            ApplicationModified.bundleFilter.putFloat(Constants.NUMBER_STAR, number_star);
            ApplicationModified.bundleFilter.putString(Constants.TEXT_MIDDLE_RECEIPT, String.valueOf(filter_middle_receipt.getText()));
            ApplicationModified.bundleFilter.putString(Constants.TEXT_LOCATION_MAX, String.valueOf(filter_location_max.getText()));


            if (ApplicationModified.StringSearch != null && !ApplicationModified.StringSearch.equals("")){
                ApplyFilter.getInstance().SetRestaurants(Search.getInstance().search(ApplicationModified.StringSearch));

                //в Apply передается состояние фильтра и в CardsAdapter посылается обновление
                CardsAdapter.getInstance().setCards(
                        ApplyFilter.getInstance().apply(ApplicationModified.bundleFilter)
                );

                ApplyFilter.getInstance().SetRestaurants(ApplicationModified.restaurantList);
            } else {
                //в Apply передается состояние фильтра и в CardsAdapter посылается обновление
                CardsAdapter.getInstance().setCards(
                        ApplyFilter.getInstance().apply(ApplicationModified.bundleFilter)
                );
            }

        }
    };
    private final View.OnClickListener ClearButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            filter_location_max.setText("");
            filter_middle_receipt.setText("");
            RefreshFilterScore(0);
            filter_with_itself.setChecked(false);
            filter_fast_food.setChecked(false);
            filter_sale.setChecked(false);

            filter_apply_button.performClick();
        }
    };
    private final View.OnClickListener Filter_score = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.icon_star_1:
                    RefreshFilterScore(1);
                    break;
                case R.id.icon_star_2:
                    RefreshFilterScore(2);
                    break;
                case R.id.icon_star_3:
                    RefreshFilterScore(3);
                    break;
                case R.id.icon_star_4:
                    RefreshFilterScore(4);
                    break;
                case R.id.icon_star_5:
                    RefreshFilterScore(5);
                    break;
            }
        }
    };
    private final TextView.OnEditorActionListener Filter_location_max = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow()
                        .getDecorView().getWindowToken(), 0); //убирает курсор в поле ввода расстояния
                v.clearFocus();
                handled = true;
            }
            return handled;
        }
    };
    private final View.OnTouchListener Click_Filter_location_max = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                getLocationPermission();
                updateLocationUI();
            }
            return false;
        }
    };
    private final TextView.OnEditorActionListener Filter_middle_receipt = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow()
                        .getDecorView().getWindowToken(), 0); //убирает курсор в поле ввода среднего чека
                v.clearFocus();
                handled = true;
            }
            return handled;
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        ApplicationModified.bundleFilter.putFloat(Constants.NUMBER_STAR, number_star);

        text_location_max = String.valueOf(filter_location_max.getText());
        ApplicationModified.bundleFilter.putString(Constants.TEXT_LOCATION_MAX, text_location_max);

        text_middle_receipt = String.valueOf(filter_middle_receipt.getText());
        ApplicationModified.bundleFilter.putString(Constants.TEXT_MIDDLE_RECEIPT, text_middle_receipt);

        tag_with_itself = filter_with_itself.isChecked();
        ApplicationModified.bundleFilter.putBoolean(Constants.TAG_WITH_ITSELF, tag_with_itself);

        tag_fast_food = filter_fast_food.isChecked();
        ApplicationModified.bundleFilter.putBoolean(Constants.TAG_FAST_FOOD, tag_fast_food);

        tag_sale = filter_sale.isChecked();
        ApplicationModified.bundleFilter.putBoolean(Constants.TAF_SALE, tag_sale);


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        number_star = ApplicationModified.bundleFilter.getFloat(Constants.NUMBER_STAR);
        RefreshFilterScore(number_star);

        text_location_max = ApplicationModified.bundleFilter.getString(Constants.TEXT_LOCATION_MAX);
        this.filter_location_max.setText(text_location_max);

        text_middle_receipt = ApplicationModified.bundleFilter.getString(Constants.TEXT_MIDDLE_RECEIPT);
        this.filter_middle_receipt.setText(text_middle_receipt);

        tag_with_itself = ApplicationModified.bundleFilter.getBoolean(Constants.TAG_WITH_ITSELF);
        this.filter_with_itself.setChecked(tag_with_itself);

        tag_fast_food = ApplicationModified.bundleFilter.getBoolean(Constants.TAG_FAST_FOOD);
        this.filter_fast_food.setChecked(tag_fast_food);

        tag_sale = ApplicationModified.bundleFilter.getBoolean(Constants.TAF_SALE);
        this.filter_sale.setChecked(tag_sale);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        ApplicationModified.bundleFilter.putFloat(Constants.NUMBER_STAR, number_star);

        text_location_max = String.valueOf(filter_location_max.getText());
        ApplicationModified.bundleFilter.putString(Constants.TEXT_LOCATION_MAX, text_location_max);

        text_middle_receipt = String.valueOf(filter_middle_receipt.getText());
        ApplicationModified.bundleFilter.putString(Constants.TEXT_MIDDLE_RECEIPT, text_middle_receipt);

        tag_with_itself = filter_with_itself.isChecked();
        ApplicationModified.bundleFilter.putBoolean(Constants.TAG_WITH_ITSELF, tag_with_itself);

        tag_fast_food = filter_fast_food.isChecked();
        ApplicationModified.bundleFilter.putBoolean(Constants.TAG_FAST_FOOD, tag_fast_food);

        tag_sale = filter_sale.isChecked();
        ApplicationModified.bundleFilter.putBoolean(Constants.TAF_SALE, tag_sale);
    }

    //обновляет количество звезд в фильтре
    private void RefreshFilterScore(float number_star){
        score_filter_star1.setImageResource(R.drawable.icon_score_clear);
        score_filter_star2.setImageResource(R.drawable.icon_score_clear);
        score_filter_star3.setImageResource(R.drawable.icon_score_clear);
        score_filter_star4.setImageResource(R.drawable.icon_score_clear);
        score_filter_star5.setImageResource(R.drawable.icon_score_clear);
        switch ((int) number_star) {
            case 0:
                this.number_star = 0;
                break;
            case 1:
                score_filter_star1.setImageResource(R.drawable.icons_score);
                this.number_star = 1;
                break;
            case 2:
                score_filter_star1.setImageResource(R.drawable.icons_score);
                score_filter_star2.setImageResource(R.drawable.icons_score);
                this.number_star = 2;
                break;
            case 3:
                score_filter_star1.setImageResource(R.drawable.icons_score);
                score_filter_star2.setImageResource(R.drawable.icons_score);
                score_filter_star3.setImageResource(R.drawable.icons_score);
                this.number_star = 3;
                break;
            case 4:
                score_filter_star1.setImageResource(R.drawable.icons_score);
                score_filter_star2.setImageResource(R.drawable.icons_score);
                score_filter_star3.setImageResource(R.drawable.icons_score);
                score_filter_star4.setImageResource(R.drawable.icons_score);
                this.number_star = 4;
                break;
            case 5:
                score_filter_star1.setImageResource(R.drawable.icons_score);
                score_filter_star2.setImageResource(R.drawable.icons_score);
                score_filter_star3.setImageResource(R.drawable.icons_score);
                score_filter_star4.setImageResource(R.drawable.icons_score);
                score_filter_star5.setImageResource(R.drawable.icons_score);
                this.number_star = 5;
                break;
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(ApplicationModified.contextApplication,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            filter_location_max.setEnabled(true);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                locationPermissionGranted = false;
                if (isGranted) {
                    locationPermissionGranted = true;

                    filter_location_max.setEnabled(true);
                } else {
                    Snackbar.make(layout, R.string.permission_rationale_location_from_map,
                            Snackbar.LENGTH_LONG)
                            .show();
                    filter_location_max.setEnabled(false);
                }
                updateLocationUI();
            });

    private void updateLocationUI() {
        if (locationPermissionGranted) {
            getDeviceLocation();
        } else {
            lastKnownLocation = null;
        }
    }

    private void getDeviceLocation() {

        Places.initialize(ApplicationModified.contextApplication, "${MAPS_API_KEY}");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            ApplicationModified.myLocation = new com.google.maps.model.LatLng(
                                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

}
