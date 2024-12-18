package com.example.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class MovieAdapter extends BaseAdapter {

    private Context context;
    private JSONArray moviesArray;
    private JSONArray originalMoviesArray; // Store the original list of movies
    private OnMovieClickListener listener;

    // Constructor to accept the listener and movies
    public MovieAdapter(Context context, JSONArray moviesArray, OnMovieClickListener listener) {
        this.context = context;
        this.moviesArray = moviesArray;
        this.originalMoviesArray = moviesArray;  // Store the original list for search functionality
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return moviesArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return moviesArray.getJSONObject(position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the item_movie layout if it's not reused
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        }

        // Get the views from the layout
        TextView movieTitleText = convertView.findViewById(R.id.movie_title);
        ImageView movieImageView = convertView.findViewById(R.id.movie_image);

        try {
            // Get the movie data from the JSON array
            JSONObject movie = moviesArray.getJSONObject(position);
            String title = movie.getString("title");
            String imageUrl = movie.getString("poster_path");  // Assuming the backend returns a poster path

            // Set the movie title
            movieTitleText.setText(title);

            // Use Picasso to load the movie image
            String imageUrlFull = "https://image.tmdb.org/t/p/w500" + imageUrl;  // Assuming this is the base URL for the image
            Picasso.get()
                    .load(imageUrlFull)
                    .placeholder(R.drawable.ic_launcher_foreground)  // A placeholder image
                    .into(movieImageView);

            // Set up the click listener for each movie item
            convertView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMovieClick(moviesArray, position);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    // Method to update the movies list based on the search query
    public void updateMovies(JSONArray newMovies) {
        this.moviesArray = newMovies;  // Set the filtered movies array
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }

    // Create an interface to handle item click events
    public interface OnMovieClickListener {
        void onMovieClick(JSONArray moviesArray, int position);
    }
}

