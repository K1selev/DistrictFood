package ru.techpark.districtfood.RestaurantTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.techpark.districtfood.R;

public class FragmentFeedbackAdapter  extends RecyclerView.Adapter<FragmentFeedbackViewHolder>{

    private String[] feedbacks;

    @NonNull
    @Override
    public FragmentFeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item, parent, false);
        return new FragmentFeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentFeedbackViewHolder holder, int position) {
        String feedback = feedbacks[position];

        holder.feedback_text.setText(feedback.substring(1, feedback.length() - 4));

        String feedback_score = String.valueOf(feedback.charAt(feedback.length() - 2));
        holder.feedback_score.setText(feedback_score);
    }

    @Override
    public int getItemCount() {
        if (feedbacks != null) {
            return feedbacks.length;
        } else return 0;
    }

    public void setFeedbacks(String feedbacks)
    {
        if (!feedbacks.equals("")) {
            this.feedbacks = feedbacks.split("</1337/>");
        }
        notifyDataSetChanged();
    }
}
