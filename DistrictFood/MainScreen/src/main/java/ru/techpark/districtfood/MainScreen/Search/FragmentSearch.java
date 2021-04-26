package ru.techpark.districtfood.MainScreen.Search;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.techpark.districtfood.MainScreen.CardsPreview.CardsAdapter;
import ru.techpark.districtfood.MainScreen.CardsPreview.FragmentCards;
import ru.techpark.districtfood.R;
import ru.techpark.districtfood.databinding.FragmentSearchBinding;

public class FragmentSearch extends Fragment{

    private EditText search;
    public FragmentSearchBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search = (EditText) getActivity().findViewById(R.id.search);
        search.setOnEditorActionListener(Search_Input);
    }

    // обрабатываем нажатие кнопки поиска
    private final TextView.OnEditorActionListener Search_Input = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                v.clearFocus();

                //в Search передается строка из поиска и в CardsAdapter посылается обновление
                CardsAdapter.getInstance().setCards(Search.getInstance().search(v.getText().toString()),
                            FragmentCards.getInstance().GetCardsViewModel());
                return true;
            }
            return false;
        }
    };
}
