package com.example.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// Comment
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<String> commentsList;
    private Context context;
    private boolean[] likedState; // Array to track if each comment is liked

    public CommentsAdapter(List<String> commentsList, Context context) {
        this.commentsList = commentsList;
        this.context = context;
        likedState = new boolean[commentsList.size()]; // Initialize liked state for each comment
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView commentTextView;
        public TextView likeCountTextView;
        public ImageButton likeButton;

        public ViewHolder(View view) {
            super(view);
            commentTextView = view.findViewById(R.id.comment_text);
            likeCountTextView = view.findViewById(R.id.like_count);
            likeButton = view.findViewById(R.id.like_button);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String comment = commentsList.get(position);
        holder.commentTextView.setText(comment);

        // Initialize like count (example: starting at 0)
        AtomicInteger likeCount = new AtomicInteger(Integer.parseInt(holder.likeCountTextView.getText().toString()));

        // Set initial like icon state
        holder.likeButton.setImageResource(
                likedState[position] ? R.drawable.baseline_thumb_up_alt : R.drawable.baseline_thumb_up_off_alt);

        // Set up click listener for like button
        holder.likeButton.setOnClickListener(v -> {
            // Toggle liked state
            likedState[position] = !likedState[position];

            // Update like count and icon
            if (likedState[position]) {
                likeCount.getAndIncrement();
                holder.likeButton.setImageResource(R.drawable.baseline_thumb_up_alt);
            } else {
                likeCount.getAndDecrement();
                holder.likeButton.setImageResource(R.drawable.baseline_thumb_up_off_alt);
            }

            holder.likeCountTextView.setText(String.valueOf(likeCount.get()));
        });
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }
}
