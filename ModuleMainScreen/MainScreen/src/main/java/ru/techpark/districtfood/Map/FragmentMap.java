package ru.techpark.districtfood.Map;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.MainActivity;
import ru.techpark.districtfood.R;

public class FragmentMap extends Fragment {

    private MapViewModel mapViewModel;

    public FragmentMap(){
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

        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapViewModel.getSomething().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("test", s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            String name = arguments.getString(Constants.NAME_RESTAURANT);
            MapAction.getInstance().Router(Constants.ACTION_ROUTE_OF_RESTAURANT, name);
        } else {
            MapAction.getInstance().Router(Constants.ACTION_RESTAURANT_AROUND, null);
        }

        TextView textView = getActivity().findViewById(R.id.action);
        textView.setText(MapAction.getInstance().action());
    }

    /*public static FragmentMap sInstance;
    public synchronized static FragmentMap getInstance(String action, String name){
        if (sInstance == null) {
            sInstance = new FragmentMap();
        }
        return sInstance;
    }*/
}
