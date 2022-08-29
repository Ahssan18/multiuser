package com.food.multiuser.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.food.multiuser.Model.FeedBack;
import com.food.multiuser.R;

import java.util.List;

public class AdapterFeedback extends RecyclerView.Adapter<AdapterFeedback.CustomFeedback> {
    private List<FeedBack> feedbacks;

    public AdapterFeedback(List<FeedBack> feedbacks) {
        this.feedbacks = feedbacks;
    }

    @NonNull
    @Override
    public CustomFeedback onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feedback, parent, false);
        return new CustomFeedback(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomFeedback holder, int position) {
        setData(feedbacks.get(position), holder);
    }

    private void setData(FeedBack feedback, CustomFeedback holder) {
        holder.ratingBar.setRating(feedback.getRating());
        holder.feedbackMsg.setText(feedback.getMessage());
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    protected class CustomFeedback extends RecyclerView.ViewHolder {

        private RatingBar ratingBar;
        private TextView feedbackMsg;

        public CustomFeedback(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            feedbackMsg = itemView.findViewById(R.id.tv_feedback);
        }
    }
}
