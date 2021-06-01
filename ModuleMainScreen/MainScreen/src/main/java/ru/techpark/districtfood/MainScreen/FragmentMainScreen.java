package ru.techpark.districtfood.MainScreen;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.techpark.districtfood.ApplicationModified;
import ru.techpark.districtfood.MainScreen.CardsPreview.CardsAdapter;
import ru.techpark.districtfood.MainScreen.CardsPreview.FragmentCards;
import ru.techpark.districtfood.MainScreen.Filter.ApplyFilter;
import ru.techpark.districtfood.MainScreen.Filter.FragmentFilter;
import ru.techpark.districtfood.MainScreen.Search.Search;
import ru.techpark.districtfood.R;

public class FragmentMainScreen extends Fragment{

    private MainScreenViewModel mainScreenViewModel;
    private EditText search;
    private ImageButton filter_button;
    private ImageButton clear_search;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainScreenViewModel =
                new ViewModelProvider(this).get(MainScreenViewModel.class);
        mainScreenViewModel.getSomething().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("test", s);
            }
        });
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);

        progressBar = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        search = view.findViewById(R.id.search);
        if (ApplicationModified.StringSearch != null) {
            search.setText(ApplicationModified.StringSearch);
        }
        search.setOnEditorActionListener(Search_Input);

        filter_button = view.findViewById(R.id.filter_button);
        filter_button.setOnClickListener(Filter_button);

        clear_search = view.findViewById(R.id.btn_clear_search);
        clear_search.setOnClickListener(Clear_search_button);

        FragmentCards.getInstance().onCreateView(view, requireContext());

    }


    @Override
    public void onResume() {
        super.onResume();
        FragmentCards.getInstance().onResume(progressBar,
                getActivity(), getViewLifecycleOwner());

        if (ApplicationModified.filter_button_flag) {
            Filter_View_On(filter_button);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        FragmentCards.getInstance().onViewStateRestored();
    }

    @Override
    public void onStop() {
        super.onStop();
        FragmentCards.getInstance().onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // обрабатываем нажатие кнопки поиска
    private final TextView.OnEditorActionListener Search_Input = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                v.clearFocus();

                ApplicationModified.StringSearch = v.getText().toString();

                Search.getInstance().SetRestaurants(ApplyFilter.getInstance().apply(ApplicationModified.bundleFilter));

                //в Search передается строка из поиска и в CardsAdapter посылается обновление
                CardsAdapter.getInstance().setCards(
                        Search.getInstance().search(v.getText().toString())
                );

                Search.getInstance().SetRestaurants(ApplicationModified.restaurantList);
                return true;
            }
            return false;
        }
    };

    //стирается поиск
    private final View.OnClickListener Clear_search_button = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            search.setText("");
            search.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
        }
    };

    //открывается поле фильтра
    private final View.OnClickListener Filter_button = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {

            ApplicationModified.filter_button_flag = !ApplicationModified.filter_button_flag;

            if (ApplicationModified.filter_button_flag) {
                Filter_View_On((ImageButton) view);
            }
            else {
                Filter_View_Off((ImageButton) view);
            }
        }
    };

    private void Filter_View_On(ImageButton imageButton){
        imageButton.setImageResource(R.drawable.filter_button_up);

        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.filter_view) == null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.filter_view, new FragmentFilter())
                    .commit();
        } else {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.filter_view))
                    .add(R.id.filter_view, new FragmentFilter())
                    .commit();
        }


    }

    private void Filter_View_Off(ImageButton imageButton) {
        imageButton.setImageResource(R.drawable.filter_button_down);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FragmentFilter fragmentFilter = (FragmentFilter) fragmentManager.
                findFragmentById(R.id.filter_view);
        if (fragmentFilter != null) {
            transaction.remove(fragmentFilter);
        }
        transaction.commit();
    }

    public static FragmentMainScreen sInstance;
    public FragmentMainScreen() {

    }
    public synchronized static FragmentMainScreen getInstance(){
        if (sInstance == null) {
            sInstance = new FragmentMainScreen();
        }
        return sInstance;
    }
}
