package com.example.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontend.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdvancedSearchResultsFragment extends Fragment {

    private static final String ARG_DATA = "data";

    private GridView movieGridView;
    private MovieAdapter movieAdapter;
    private JSONArray moviesArray;

    private static final String ARG_MOVIES_ARRAY = "moviesArray";

    public AdvancedSearchResultsFragment() {
        // Required empty public constructor
    }

    public static AdvancedSearchResultsFragment newInstance(String moviesArray) {
        AdvancedSearchResultsFragment fragment = new AdvancedSearchResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIES_ARRAY, moviesArray);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advanced_search_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieGridView = view.findViewById(R.id.movie_grid);

        if (getArguments() != null) {
            try {
                // Retrieve the movies array from the arguments
                String moviesArrayString = getArguments().getString(ARG_MOVIES_ARRAY);
                moviesArray = new JSONArray(moviesArrayString);

                // Set up the adapter
                movieAdapter = new MovieAdapter(getContext(), moviesArray, new MovieAdapter.OnMovieClickListener() {
                    @Override
                    public void onMovieClick(JSONArray moviesArray, int position) {
                        try {
                            JSONObject selectedMovie = moviesArray.getJSONObject(position);
                            int movieId = selectedMovie.getInt("id");

                            // Open the MovieDetailFragment
                            MovieDetailFragment detailFragment = MovieDetailFragment.newInstance(movieId);
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, detailFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error processing selected movie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                movieGridView.setAdapter(movieAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error parsing movie data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
