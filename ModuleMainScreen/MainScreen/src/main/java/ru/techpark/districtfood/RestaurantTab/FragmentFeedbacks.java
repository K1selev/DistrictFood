package ru.techpark.districtfood.RestaurantTab;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ru.techpark.districtfood.Constants;
import ru.techpark.districtfood.R;

public class FragmentFeedbacks extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedbacks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        String feedbacks = arguments.getString(Constants.FEEDBACKS);

        if (recyclerView == null) {
            recyclerView = view.findViewById(R.id.cards_feedbacks);
        }

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_feedbacks);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        FragmentFeedbackAdapter fragmentFeedbackAdapter = new FragmentFeedbackAdapter();
        recyclerView.setAdapter(fragmentFeedbackAdapter);
        fragmentFeedbackAdapter.setFeedbacks(feedbacks);

        if (!feedbacks.equals("")) {
            view.findViewById(R.id.no_feedbacks).setVisibility(View.GONE);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                mSwipeRefreshLayout.setEnabled(!recyclerView
                        .canScrollVertically(-1));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onRefresh() {

    }
}
