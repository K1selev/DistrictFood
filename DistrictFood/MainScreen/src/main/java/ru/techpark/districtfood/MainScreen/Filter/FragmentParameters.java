package ru.techpark.districtfood.MainScreen.Filter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.techpark.districtfood.R;

public class FragmentParameters extends Fragment {

    private boolean filter_button_flag = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parameters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().findViewById(R.id.filter_button).setOnClickListener(Filter_button);
    }

    //открывается поле фильтра
    private final View.OnClickListener Filter_button = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (filter_button_flag) {
                Filter_View_On((ImageButton) view);
            }
            else {
                Filter_View_Off((ImageButton) view);
            }
        }
    };

    private void Filter_View_On(ImageButton imageButton){
        imageButton.setImageResource(R.drawable.filter_button_up);
        filter_button_flag = !filter_button_flag;

        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.filter_view) == null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.filter_view, new FragmentFilter())
                    .commit();
        } else {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .show(getActivity().getSupportFragmentManager().findFragmentById(R.id.filter_view))
                    .commit();
        }

    }

    private void Filter_View_Off(ImageButton imageButton) {
        imageButton.setImageResource(R.drawable.filter_button_down);
        filter_button_flag = !filter_button_flag;

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FragmentFilter fragmentFilter = (FragmentFilter) fragmentManager.
                findFragmentById(R.id.filter_view);
        if (fragmentFilter != null) {
            transaction.hide(fragmentFilter);
        }
        transaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FragmentFilter fragmentFilter = (FragmentFilter) fragmentManager.
                findFragmentById(R.id.filter_view);
        if (fragmentFilter != null) {
            transaction.remove(fragmentFilter);
        }
        transaction.commit();
    }
}
