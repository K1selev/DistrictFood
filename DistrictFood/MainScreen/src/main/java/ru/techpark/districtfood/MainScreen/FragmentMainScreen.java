package ru.techpark.districtfood.MainScreen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.techpark.districtfood.MainScreen.CardsPreview.FragmentCards;
import ru.techpark.districtfood.MainScreen.Filter.FragmentParameters;
import ru.techpark.districtfood.MainScreen.Search.FragmentSearch;
import ru.techpark.districtfood.R;
import ru.techpark.districtfood.databinding.FragmentMainScreenBinding;

public class FragmentMainScreen extends Fragment {

    private MainScreenViewModel mainScreenViewModel;
    public FragmentMainScreenBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainScreenViewModel =
                new ViewModelProvider(this).get(MainScreenViewModel.class);
        mBinding = FragmentMainScreenBinding.inflate(inflater, container, false);
        mainScreenViewModel.getSomething().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("test", s);
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_search) == null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_search, new FragmentSearch())
                    .commit();
        }
        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_parameters) == null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_parameters, new FragmentParameters())
                    .commit();
        }
        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_cards) == null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_cards, FragmentCards.getInstance())
                    .commit();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        FragmentSearch fragmentSearch = (FragmentSearch) fragmentManager.
                findFragmentById(R.id.fragment_search);
        if (fragmentSearch != null) {
            transaction.remove(fragmentSearch);
        }

        FragmentParameters fragmentParameters = (FragmentParameters) fragmentManager.
                findFragmentById(R.id.fragment_parameters);
        if (fragmentParameters != null) {
            transaction.remove(fragmentParameters);
        }

        FragmentCards fragmentCards = (FragmentCards) fragmentManager.
                findFragmentById(R.id.fragment_cards);
        if (fragmentCards != null) {
            transaction.remove(fragmentCards);
        }

        transaction.commit();
    }
}
