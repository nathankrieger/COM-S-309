package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.frontend.models.Movie;
import com.example.frontend.models.User;
import com.squareup.picasso.Picasso;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends Fragment implements WebSocketListener {

    private static final String ARG_MOVIE_ID = "movieId";
    private int movieId;

    // UI elements
    private ImageView moviePosterImageView;
    private TextView movieTitleTextView;
    private TextView movieReleaseDateTextView;
    private TextView movieViewsTextView;
    private TextView movieDescriptionTextView;
    private TextView movieGenresTextView;
    private EditText commentInput;
    private Button submitCommentButton;
    private Button watchTrailerButton;
    private RecyclerView commentsRecyclerView;

    private MovieAdapter recommendedMovieAdapter;

    private List<String> commentsList = new ArrayList<>();

    private CommentsAdapter commentsAdapter;

    private RequestQueue requestQueue;

    private GridView movieGridView;


    ImageButton bookmarkButton;
    boolean isBookmarked = false; // Initial state; consider loading from saved state.


    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(int movieId) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getInt(ARG_MOVIE_ID);
        }

        // Setup WebSocket connection
        setupWebSocketConnection();

        // Send movie ID via WebSocket
        sendMovieIdMessage(movieId);
    }

    private void setupWebSocketConnection() {
        String userName = UserManager.getInstance().getUser().getUsername();
        String serverUrl = "ws://coms-3090-022.class.las.iastate.edu:8080/viewCount/" + movieId;

        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(this);
    }

    private void sendMovieIdMessage(int movieId) {
        WebSocketManager.getInstance().sendMessage(String.valueOf(movieId));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Initialize UI elements
        moviePosterImageView = view.findViewById(R.id.movie_poster);
        movieTitleTextView = view.findViewById(R.id.movie_title);
        movieViewsTextView = view.findViewById(R.id.movie_views);
        movieReleaseDateTextView = view.findViewById(R.id.movie_release_date);
        movieDescriptionTextView = view.findViewById(R.id.movie_description);
        movieGenresTextView = view.findViewById(R.id.movie_genres);

        //recommendedMoviesRecyclerView = view.findViewById(R.id.recommended_movies_recycler_view);

        //recommendedMoviesAdapter = new RecommendedMoviesAdapter(recommendedMoviesList, null);
        //recommendedMoviesRecyclerView.setAdapter(recommendedMoviesAdapter);

        //recommendedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        commentInput = view.findViewById(R.id.comment_input);
        submitCommentButton = view.findViewById(R.id.submit_comment_button);
        commentsRecyclerView = view.findViewById(R.id.comments_recycler_view);

        // Setup RecyclerView
        commentsAdapter = new CommentsAdapter(commentsList, null);
        commentsRecyclerView.setAdapter(commentsAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        movieGridView = view.findViewById(R.id.recommended_movie_grid);

        // Initialize UI elements
        watchTrailerButton = view.findViewById(R.id.watch_trailer_button);

        // Set click listener for the button
        watchTrailerButton.setOnClickListener(v -> fetchTrailerUrl());




        // Initialize the RequestQueue only once
        requestQueue = Volley.newRequestQueue(getContext());

        // Fetch the movie details and image
        fetchMovieDetails(movieId);
        fetchMovieImage(movieId);
        fetchMovieViewCount(movieId);
        fetchComments();
        fetchRecommendedMovies(movieId);

        // Initialize UI elements
        bookmarkButton = view.findViewById(R.id.bookmark_button);

        // Initialize other UI components, omitted for brevity...

        // Fetch the initial bookmark state
        fetchBookmark();

        // Set up the bookmark button click listener
        bookmarkButton.setOnClickListener(v -> {
            // Toggle the bookmark status
            isBookmarked = !isBookmarked;
            updateBookmarkIcon();

            // Update bookmark status on the server
            if (isBookmarked) {
                addBookmark();
            } else {
                removeBookmark();
            }
        });

        submitCommentButton.setOnClickListener(v -> {
            String commentText = commentInput.getText().toString();
            if (!commentText.isEmpty()) {
                submitComment(commentText);
                commentInput.setText("");  // Clear input
            } else {
                Toast.makeText(getContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        /*
        isBookmarked = true;
        updateBookmarkIcon();*/


        return view;
    }

    // Function to fetch movie details like title and release date
    private void fetchMovieDetails(int movieId) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/api/movies/" + movieId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        // Parse the response
                        String title = response.getString("title");
                        String releaseDate = response.getString("release_date");
                        String description = response.getString("overview");

                        StringBuilder genresBuilder = new StringBuilder();
                        JSONArray genresArray = response.getJSONArray("genres");
                        for (int i = 0; i < genresArray.length(); i++) {
                            JSONObject genre = genresArray.getJSONObject(i);
                            genresBuilder.append(genre.getString("name"));
                            if (i < genresArray.length() - 1) {
                                genresBuilder.append(", ");
                            }
                        }
                        String genres = genresBuilder.toString();

                        // Update UI elements
                        movieTitleTextView.setText(title);
                        movieReleaseDateTextView.setText(releaseDate);
                        movieDescriptionTextView.setText(description);
                        movieGenresTextView.setText(genres);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        movieTitleTextView.setText("Error parsing movie details.");
                    }
                },
                error -> movieTitleTextView.setText("Failed to load movie details.")
        );

        requestQueue.add(jsonObjectRequest);
    }

    // Function to fetch the movie image URL and load it into the ImageView
    private void fetchMovieImage(int movieId) {
        String imageUrl = "http://coms-3090-022.class.las.iastate.edu:8080/api/movies/image/" + movieId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, imageUrl,
                response -> Picasso.get().load(response)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .into(moviePosterImageView),
                error -> moviePosterImageView.setImageResource(R.drawable.placeholder_image)
        );

        requestQueue.add(stringRequest);
    }

    private void fetchRecommendedMovies(int movieId) {
        MovieApiClient movieApiClient = new MovieApiClient(getContext());
        movieApiClient.fetchRecommendedMovies(movieId, new MovieApiClient.MovieApiResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray moviesArray = response.getJSONArray("results");

                    // Store the original movies array to allow filtering
                    //originalMoviesArray = moviesArray;

                    // Set up the adapter or update the existing one
                    if (recommendedMovieAdapter == null) {
                        recommendedMovieAdapter = new MovieAdapter(getContext(), moviesArray, new MovieAdapter.OnMovieClickListener() {
                            @Override
                            public void onMovieClick(JSONArray moviesArray, int position) {
                                try {
                                    JSONObject selectedMovie = (JSONObject) recommendedMovieAdapter.getItem(position);
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
                        movieGridView.setAdapter(recommendedMovieAdapter);
                    } else {
                        recommendedMovieAdapter.updateMovies(moviesArray);  // Update existing adapter with new movies
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

    private void fetchMovieViewCount(int movieId) {
            String url = "http://coms-3090-022.class.las.iastate.edu:8080/ViewCountById/" + movieId;

            // Make a GET request to fetch the view count
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, url, null,
                    response -> {
                        try {
                            // Parse the response to get the view count
                            int viewCount = response.getInt("viewCount");

                            // Update the UI with the view count
                            movieViewsTextView.setText("Views: " + viewCount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            movieViewsTextView.setText("Error loading views.");
                        }
                    },
                    error -> {
                        // Handle error and set fallback text
                        movieViewsTextView.setText("Failed to load views.");
                        Log.e("fetchMovieViewCount", "Error fetching movie view count: " + error.getMessage());
                    }
            );

        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest);
    }

    private void fetchComments() {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/comments/movie/" + movieId;  // Replace with your API endpoint
        List<String> commentsList = new ArrayList<>();  // Initialize an empty list

        // Use Volley to make an API request
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        // Parse the response from the API (assuming it's JSON)
                        JSONArray commentsArray = new JSONArray(response);
                        for (int i = 0; i < commentsArray.length(); i++) {
                            JSONObject commentObject = commentsArray.getJSONObject(i);
                            String comment = commentObject.getString("text");  // Adjust key based on your API response
                            commentsList.add(comment);
                        }
                        // Update RecyclerView Adapter
                        CommentsAdapter adapter = new CommentsAdapter(commentsList, null);
                        commentsRecyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Handle error
            Log.e("API Error", error.toString());
        });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(getContext()).add(request);
    }


    // Method to update the bookmark icon based on isBookmarked
    private void updateBookmarkIcon() {
        int icon = isBookmarked ? R.drawable.baseline_bookmark : R.drawable.baseline_bookmark_border;
        bookmarkButton.setImageResource(icon);
    }

    // Method to fetch bookmark status from the server
    /*private void fetchBookmark() {
        int userID = UserManager.getInstance().getUser().getUserID();
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/bookmarks/userId/movieId/" + userID + "/" + movieId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    int id = -1;
                    try {
                        id = response.getInt("id");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    isBookmarked = id != -1;  // Set true if id is valid, false if -1
                    updateBookmarkIcon();
                },
                error -> Log.e("fetchBookmark", "Error fetching bookmark: " + error.getMessage())
        );

        requestQueue.add(jsonObjectRequest);
    }*/

    private void fetchBookmark() {
        int userID = UserManager.getInstance().getUser().getUserID();
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/bookmarks/userId/movieId/" + userID + "/" + movieId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    int id = -1;
                    try {
                        id = response.getInt("id");
                    } catch (JSONException e) {
                        //String err = "Error parsing bookmark response.";
                        //Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                    isBookmarked = id != -1; // Set true if id is valid, false if -1
                    updateBookmarkIcon();

                    // Show success toast
                    /*if (isBookmarked) {
                        Toast.makeText(getContext(), "Bookmark fetched successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "No bookmark found for this movie.", Toast.LENGTH_SHORT).show();
                    }*/
                },
                error -> {
                    //Toast.makeText(getContext(), "Error fetching bookmark: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }

        );

        requestQueue.add(jsonObjectRequest);
    }


    // Method to add a bookmark
    private void addBookmark() {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/bookmarks/create/" + UserManager.getInstance().getUser().getUserID() + "/" + movieId;
        JSONObject bookmark = new JSONObject();
        try {
            bookmark.put("userId", UserManager.getInstance().getUser().getUserID());
            bookmark.put("movieId", movieId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, bookmark,
                response -> Toast.makeText(getContext(), "Bookmarked!", Toast.LENGTH_SHORT).show(),
                error -> Log.e("addBookmark", "Error adding bookmark: " + error.getMessage())
        );

        requestQueue.add(request);
    }

    // Method to remove a bookmark
    private void removeBookmark() {
        int userID = UserManager.getInstance().getUser().getUserID();
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/bookmarks/delete/" + userID + "/" + movieId;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> Toast.makeText(getContext(), "Bookmark removed!", Toast.LENGTH_SHORT).show(),
                error -> Log.e("removeBookmark", "Error removing bookmark: " + error.getMessage())
        );

        requestQueue.add(request);
    }

    private void submitComment(String commentText) {

        User user = UserManager.getInstance().getUser();

        String url = "http://coms-3090-022.class.las.iastate.edu:8080/comments/create";
        JSONObject comment = new JSONObject();
        try {
            comment.put("userid", user.getUserID());
            comment.put("movieId", movieId);
            comment.put("text", commentText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, comment,
                response -> {
                    Toast.makeText(getContext(), "Comment submitted!", Toast.LENGTH_SHORT).show();
                    fetchComments();  // Refresh comments on success
                },
                error -> {
                    Log.e("SubmitCommentError", "Error submitting comment: " + error.getMessage());
                    Toast.makeText(getContext(), "Failed to submit comment. Please try again.", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }

    /*private void likeComment(int commentID) {

        String url = "http://coms-3090-022.class.las.iastate.edu:8080/comments/like/" + commentID;  // Replace with API URL
        JSONObject comment = new JSONObject();
        try {
            comment.put("userid", user.getUserID());
            comment.put("movieId", movieId);
            comment.put("text", commentText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, comment,
                response -> fetchComments(),  // Refresh comments on success
                error -> Log.e("SubmitCommentError", "Error submitting comment: " + error.getMessage()));

        Volley.newRequestQueue(getContext()).add(request);
    }*/

    private void fetchTrailerUrl() {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/api/movies/trailer/" + movieId;

        // Make a GET request to fetch the trailer URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Log the fetched URL to ensure it's correct
                    Log.d("MovieDetailActivity", "Fetched trailer URL: " + response);

                    // On success, open the trailer activity and pass the trailer URL
                    openTrailerActivity(response);
                },
                error -> {
                    // Handle error if the trailer is not available
                    Log.e("MovieDetailActivity", "Error fetching trailer: " + error.getMessage());
                    Toast.makeText(getContext(), "Trailer not found", Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the request queue
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    // Open the TrailerActivity with the fetched trailer URL
    private void openTrailerActivity(String trailerUrl) {
        Intent intent = new Intent(getActivity(), TrailerActivity.class);
        intent.putExtra("trailerUrl", trailerUrl);  // Pass the trailer URL to the TrailerActivity
        startActivity(intent);
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        WebSocketManager.getInstance().sendMessage(movieId + "");
    }

    @Override
    public void onWebSocketMessage(String message) { }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) { }

    @Override
    public void onWebSocketError(Exception ex) { }
}
