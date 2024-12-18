package com.example.frontend;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;


import org.json.JSONArray;
import org.json.JSONObject;

public class MovieDashboardFragment extends Fragment {

    private GridView movieGridView;
    private MovieAdapter movieAdapter;
    private JSONArray originalMoviesArray;  // To store the original movie list for filtering
    private int currentPage = 1;  // Default to the first page
    private int totalPages = 3;  // Initially set, will be updated from the API

    private Button advancedSearchButton;

    public MovieDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_dashboard, container, false);

        movieGridView = view.findViewById(R.id.movie_grid);  // Ensure this matches your GridView ID
        //EditText searchBar = view.findViewById(R.id.searchBar);  // Get the search bar
        advancedSearchButton = view.findViewById(R.id.advanced_search_btn);

        // Advanced Search Button functionality
        advancedSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedSearchFragment advancedSearchFragment = AdvancedSearchFragment.newInstance();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, advancedSearchFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Fetch the movies for the first page
        fetchMovies(currentPage);

        // Set up pagination buttons
        Button nextPageButton = view.findViewById(R.id.next_page_button);
        Button prevPageButton = view.findViewById(R.id.prev_page_button);

        nextPageButton.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                fetchMovies(currentPage);
            }
        });

        prevPageButton.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                fetchMovies(currentPage);
            }
        });

        // Set up the search bar functionality
        /*searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString().toLowerCase();
                filterMovies(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
*/
        return view;
    }

    // Fetch movies for a given page
    private void fetchMovies(int page) {
        MovieApiClient movieApiClient = new MovieApiClient(getContext());
        movieApiClient.fetchPopularMovies(page, new MovieApiClient.MovieApiResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray moviesArray = response.getJSONArray("results");
                    totalPages = response.getInt("total_pages");  // Get total pages from the API response

                    // Store the original movies array to allow filtering
                    originalMoviesArray = moviesArray;

                    // Set up the adapter or update the existing one
                    if (movieAdapter == null) {
                        movieAdapter = new MovieAdapter(getContext(), moviesArray, new MovieAdapter.OnMovieClickListener() {
                            @Override
                            public void onMovieClick(JSONArray moviesArray, int position) {
                                try {
                                    JSONObject selectedMovie = (JSONObject) movieAdapter.getItem(position);
                                    int movieId = selectedMovie.getInt("id");

                                    MovieDetailFragment detailFragment = MovieDetailFragment.newInstance(movieId);
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, detailFragment)
                                            .addToBackStack(null)
                                            .commit();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Error processing selected movie", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        movieGridView.setAdapter(movieAdapter);
                    } else {
                        movieAdapter.updateMovies(moviesArray);  // Update existing adapter with new movies
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error parsing movie data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "Error fetching movies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Filter movies based on the search query
    private void filterMovies(String query) {
        try {
            // If the search query is empty, show all movies
            if (query.isEmpty()) {
                movieAdapter.updateMovies(originalMoviesArray);  // Show the full list again
                return;
            }

            // Filter movies based on title
            JSONArray filteredMovies = new JSONArray();
            for (int i = 0; i < originalMoviesArray.length(); i++) {
                JSONObject movie = originalMoviesArray.getJSONObject(i);
                String title = movie.getString("title").toLowerCase();
                if (title.contains(query)) {
                    filteredMovies.put(movie);
                }
            }

            // Update the adapter with the filtered list
            movieAdapter.updateMovies(filteredMovies);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error filtering movies", Toast.LENGTH_SHORT).show();
        }
    }
}