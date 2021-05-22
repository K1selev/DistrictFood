package ru.techpark.districtfood.RestaurantTab;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.MainActivity;
import ru.techpark.districtfood.R;

public class FragmentRestaurantTab extends Fragment {

    private TextView textView;
    private String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ApplicationModified.enabled_recyclerView = false;

        return inflater.inflate(R.layout.fragment_restaurant_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = view.findViewById(R.id.Name);

    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        name = arguments.getString(Constants.NAME_RESTAURANT);

        textView.setText(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationModified.enabled_recyclerView = true;
    }

    public FragmentRestaurantTab(){}
    public static FragmentRestaurantTab sInstance;
    public synchronized static FragmentRestaurantTab getInstance(){
        if (sInstance == null) {
            sInstance = new FragmentRestaurantTab();
        }
        return sInstance;
    }

    public void setName(String name){
        this.name = name;
    }


}
