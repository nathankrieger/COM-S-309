package com.example.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

// Recommended Movies Adapter
public class RecommendedMoviesAdapter extends RecyclerView.Adapter<RecommendedMoviesAdapter.ViewHolder> {

    private List<Movie> movieList;  // Assuming a Movie class with title and poster URL
    private Context context;

    public RecommendedMoviesAdapter(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePosterImageView;
        public TextView movieTitleTextView;

        public ViewHolder(View view) {
            super(view);
            moviePosterImageView = view.findViewById(R.id.movie_image);
            movieTitleTextView = view.findViewById(R.id.movie_title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // Set the movie title
        holder.movieTitleTextView.setText(movie.getTitle());

        // Load the movie poster using Picasso
        String imageUrlFull = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Picasso.get()
                .load(imageUrlFull)  // URL of the movie poster image
                .into(holder.moviePosterImageView); // Set image in the ImageView
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
