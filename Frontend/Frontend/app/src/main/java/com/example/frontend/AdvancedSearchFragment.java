package com.example.frontend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.frontend.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdvancedSearchFragment extends Fragment {

    private static final String TAG = "AdvancedSearchFragment";

    private RequestQueue requestQueue;

    public AdvancedSearchFragment() {
        // Required empty public constructor
    }

    public static AdvancedSearchFragment newInstance() {
        return new AdvancedSearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        requestQueue = Volley.newRequestQueue(getContext());


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advanced_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        CheckBox includeAdultCheckBox = view.findViewById(R.id.include_adult);
        CheckBox includeVideoCheckBox = view.findViewById(R.id.include_video);
        EditText languageEditText = view.findViewById(R.id.language);
        EditText releaseDateFromEditText = view.findViewById(R.id.primary_release_date_gte);
        EditText releaseDateToEditText = view.findViewById(R.id.primary_release_date_lte);
        EditText voteAverageMinEditText = view.findViewById(R.id.vote_average_gte);
        //EditText voteAverageMaxEditText = view.findViewById(R.id.vote_average_lte);
        EditText genresEditText = view.findViewById(R.id.with_genres);
        EditText keywordsEditText = view.findViewById(R.id.with_keywords);
        //EditText runtimeMinEditText = view.findViewById(R.id.with_runtime_gte);
        EditText runtimeMaxEditText = view.findViewById(R.id.with_runtime_lte);
        EditText yearEditText = view.findViewById(R.id.year);
        Button searchButton = view.findViewById(R.id.advanced_search_submit);

        // Set up the Search button logic
        searchButton.setOnClickListener(v -> {
            try {
                // Collect inputs
                JSONObject searchParams = new JSONObject();
                searchParams.put("include_adult", includeAdultCheckBox.isChecked());
                searchParams.put("include_video", includeVideoCheckBox.isChecked());
                searchParams.put("language", getTextOrDefault(languageEditText, "en-US"));
                searchParams.put("primary_release_date_gte", getTextOrDefault(releaseDateFromEditText, ""));
                searchParams.put("primary_release_date_lte", getTextOrDefault(releaseDateToEditText, ""));
                searchParams.put("vote_average_gte", parseNumberOrDefault(voteAverageMinEditText, -1));
                searchParams.put("vote_average_lte", -1);
                searchParams.put("with_genres", getTextOrDefault(genresEditText, ""));
                searchParams.put("with_keywords", getTextOrDefault(keywordsEditText, ""));
                searchParams.put("with_runtime_gte", -1);
                searchParams.put("with_runtime_lte", parseNumberOrDefault(runtimeMaxEditText, -1));
                searchParams.put("year", parseNumberOrDefault(yearEditText, -1));

                Log.d(TAG, "Search Params: " + searchParams.toString());

                // Make API Request
                fetchAdvancedSearchMovies(searchParams);
            } catch (Exception e) {
                Log.e(TAG, "Error creating search params: ", e);
                Toast.makeText(getContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getTextOrDefault(EditText editText, String defaultValue) {
        String text = editText.getText().toString().trim();
        return text.isEmpty() ? defaultValue : text;
    }

    private int parseNumberOrDefault(EditText editText, int defaultValue) {
        String text = editText.getText().toString().trim();
        try {
            return text.isEmpty() ? defaultValue : Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void fetchAdvancedSearchMovies(JSONObject searchParams) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/AdvancedSearch";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, searchParams,
                response -> {
                    try {
                        // Extract the array of recommended movies from the response
                        JSONArray recommendedMoviesArray = response.getJSONArray("results");


                        openResultsFragment(recommendedMoviesArray);

                    } catch (JSONException e) {
                        Log.e("fetchRecommendedMovies", "Error parsing recommended movies: " + e.getMessage());
                    }
                },
                error -> Log.e("fetchRecommendedMovies", "Error fetching recommended movies: " + error.getMessage())
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void openResultsFragment(JSONArray moviesArray) {
        // Pass the JSONArray to AdvancedSearchResultsFragment
        Fragment resultsFragment = AdvancedSearchResultsFragment.newInstance(moviesArray.toString());

        // Replace the current fragment with AdvancedSearchResultsFragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, resultsFragment); // Ensure this ID matches your layout
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
