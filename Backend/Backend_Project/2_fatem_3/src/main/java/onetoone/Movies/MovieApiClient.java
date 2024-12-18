package onetoone.Movies;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MovieApiClient {

    private static final String API_KEY = "6d50549abc8f2adc5e2b30beefa50f42";
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    // Fetch popular movie
    public Map<String, Object> fetchPopularMovies() {
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
        return restTemplate.getForObject(url, HashMap.class);
    }

    // Search movies by title
    public Map<String, Object> searchMovies(String query) {
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + query;
        return restTemplate.getForObject(url, HashMap.class);
    }

    // Fetch movie details by ID
    public Map<String, Object> fetchMovieDetails(int movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY;
        return restTemplate.getForObject(url, HashMap.class);
    }


    public String getMovieImageUrl(int movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY;
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);


        String posterPath = (String) response.get("poster_path");
        if (posterPath != null && !posterPath.isEmpty()) {
            return BASE_IMAGE_URL + posterPath;
        } else {
            return "Image not available"; // Default message if no image is available
        }
    }

    public String getMovieOverview(int movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY;
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);

        // Extract the overview if available
        String overview = (String) response.get("overview");
        if (overview != null && !overview.isEmpty()) {
            return overview;
        } else {
            return "Overview not available"; // Default message if no overview is available
        }
    }

    public Map<String, Object> fetchSimilarMovies(int movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/similar?api_key=" + API_KEY;
        return restTemplate.getForObject(url, HashMap.class);
    }

    public Map<String, Object> fetchRecommendedMovies(int movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/recommendations?api_key=" + API_KEY;
        return restTemplate.getForObject(url, HashMap.class);
    }

    public String getMovieTitle(int movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY;
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);

        // Extract the title if available
        String title = (String) response.get("title");
        if (title != null && !title.isEmpty()) {
            return title;
        } else {
            return "Title not available"; // Default message if no title is available
        }
    }

    public String getMovieReleaseDate(int movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY;
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);

        // Extract the release date if available
        String release_date = (String) response.get("release_date");
        if (release_date != null && !release_date.isEmpty()) {
            return release_date;
        } else {
            return "Release Date not available"; // Default message if no release date is available
        }
    }

    public double getMovieRating(int movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY;
        Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);

        // Extract the rating if available
        return (double) response.get("vote_average");
    }

    public int getPersonIdByName(String name) {
        String url = "https://api.themoviedb.org/3/search/person?api_key=" + API_KEY + "&query=" + name;
        HashMap<String, Object> hashMap = restTemplate.getForObject(url, HashMap.class);
        List<Map<String, Object>> results = (List<Map<String, Object>>) hashMap.get("results");
        int firstPersonId = ((Integer)results.get(0).get("id"));
        return firstPersonId;
    }

    public int getKeywordIdByName(String name) {
        String url = "https://api.themoviedb.org/3/search/keyword?api_key=" + API_KEY + "&query=" + name;
        HashMap<String, Object> hashMap = restTemplate.getForObject(url, HashMap.class);
        List<Map<String, Object>> results = (List<Map<String, Object>>) hashMap.get("results");
        int KeywordId = ((Integer)results.get(0).get("id"));
        return KeywordId;
    }

    public int getGenreIdByName(String name) {
        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY;
        HashMap<String, Object> hashMap = restTemplate.getForObject(url, HashMap.class);
        List<Map<String, Object>> genres = (List<Map<String, Object>>) hashMap.get("genres");
        for (Map<String, Object> genre : genres) {
            if (genre.get("name").toString().equalsIgnoreCase(name)) {
                return (int) genre.get("id");
            }
        }
        return -1;
    }


    public String getMovieTrailerUrl(int movieId) {
        try {
            String url = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + API_KEY;
            Map<String, Object> response = restTemplate.getForObject(url, HashMap.class);

            // Check if the response contains the "results" key
            if (response != null && response.containsKey("results")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

                // Check if there are trailers in the results
                if (results != null && !results.isEmpty()) {
                    Map<String, Object> trailer = results.get(0); // Assuming the first result is the main trailer
                    String key = (String) trailer.get("key");  // Trailer key

                    if (key != null && !key.isEmpty()) {
                        return "https://www.youtube.com/watch?v=" + key;  // Returning full YouTube trailer URL
                    }
                }
            }

            // If no trailer is found, return an appropriate message
            return "Trailer not available";

        } catch (Exception e) {
            // Log the error for debugging and return a generic error message
            e.printStackTrace();
            return "Error fetching trailer";
        }
    }

    // Fetch popular movies by page
    public Map<String, Object> fetchPopularMovies(int page) {
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY + "&page=" + page;
        return restTemplate.getForObject(url, HashMap.class);
    }


}