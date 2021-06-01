package ru.techpark.districtfood.RestaurantTab;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.techpark.districtfood.R;

public class FragmentFeedbackViewHolder extends RecyclerView.ViewHolder {

    protected TextView feedback_text;
    protected TextView feedback_score;

    public FragmentFeedbackViewHolder(@NonNull View itemView) {
        super(itemView);

        feedback_text = itemView.findViewById(R.id.feedback_text);
        feedback_score = itemView.findViewById(R.id.score_preview_feedback);
    }
}
