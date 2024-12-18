package com.example.frontend;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MovieApiClient {

    private Context context;
    private RequestQueue requestQueue;

    public MovieApiClient(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void fetchPopularMovies(int page, final MovieApiResponseListener listener) {
        // Fix URL structure if needed (e.g., use `page={page}` instead of `/page/{pageId}`)
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/api/movies/popular/page/" + page;
        Log.d("MovieApi", "Requesting URL: " + url);  // Log the URL

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("MovieApi", "Response received: " + response.toString());  // Log the response
                        listener.onSuccess(response);  // Pass the response back to the listener
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the 404 error gracefully
                if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                    Log.e("MovieApi", "Error 404 - Page not found");
                    listener.onError(error);  // Pass the error to the listener
                } else {
                    Log.e("MovieApi", "Error fetching movies", error);  // Log other errors
                    listener.onError(error);  // Pass the error to the listener
                }
            }
        });

        // Add the request to the request queue
        requestQueue.add(request);
    }

    public void fetchRecommendedMovies(int movieId, final MovieApiResponseListener listener) {
        // Fix URL structure if needed (e.g., use `page={page}` instead of `/page/{pageId}`)
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/api/movies/similar/" + movieId;
        Log.d("MovieApi", "Requesting URL: " + url);  // Log the URL

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("MovieApi", "Response received: " + response.toString());  // Log the response
                        listener.onSuccess(response);  // Pass the response back to the listener
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the 404 error gracefully
                if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                    Log.e("MovieApi", "Error 404 - Page not found");
                    listener.onError(error);  // Pass the error to the listener
                } else {
                    Log.e("MovieApi", "Error fetching movies", error);  // Log other errors
                    listener.onError(error);  // Pass the error to the listener
                }
            }
        });

        // Add the request to the request queue
        requestQueue.add(request);
    }

    // Search movies by title
    public void searchMovies(String query, final MovieApiResponseListener listener) {
        String url = "http://coms-3090-022.class.las.iastate.edu:8080/api/movies/search?query=" + query;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });

        requestQueue.add(request);
    }

    // MovieApiResponseListener interface to handle the response
    public interface MovieApiResponseListener {
        void onSuccess(JSONObject response);
        void onError(VolleyError error);
    }
}