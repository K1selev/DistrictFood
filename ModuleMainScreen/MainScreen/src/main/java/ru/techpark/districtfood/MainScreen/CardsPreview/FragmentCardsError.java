package ru.techpark.districtfood.MainScreen.CardsPreview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.techpark.districtfood.MainActivity;
import ru.techpark.districtfood.MainScreen.FragmentMainScreen;
import ru.techpark.districtfood.R;

public class FragmentCardsError extends Fragment {

    private Button BtnReplyAttempt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cards_error, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BtnReplyAttempt = view.findViewById(R.id.reply_attempt);
        BtnReplyAttempt.setOnClickListener(ReplyAttempt);

    }

    private final View.OnClickListener ReplyAttempt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentMainScreen.getInstance().Reloading();
        }
    };
}
