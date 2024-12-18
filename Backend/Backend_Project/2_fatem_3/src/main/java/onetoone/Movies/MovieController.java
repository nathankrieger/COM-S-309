package onetoone.Movies;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieApiClient movieApiClient;

    /**
     * Fetch popular movies
     *
     * @return A map containing popular movies data
     */
    @Operation(summary = "Fetch popular movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched popular movies",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })
    })
    @GetMapping("/popular")
    public Map<String, Object> getPopularMovies() {
        return movieApiClient.fetchPopularMovies();
    }

    /**
     * Search movies by title
     *
     * @param query The title to search for
     * @return A map containing movie search results
     */
    @Operation(summary = "Search movies by title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movie search results",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })
    })
    @GetMapping("/search")
    public Map<String, Object> searchMovies(@RequestParam String query) {
        return movieApiClient.searchMovies(query);
    }

    /**
     * Fetch movie details by ID
     *
     * @param id The ID of the movie
     * @return A map containing movie details
     */
    @Operation(summary = "Fetch movie details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movie details",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })
    })
    @GetMapping("/{id}")
    public Map<String, Object> getMovieDetails(@PathVariable int id) {
        return movieApiClient.fetchMovieDetails(id);
    }

    /**
     * Fetch the movie image URL by ID
     *
     * @param id The ID of the movie
     * @return The image URL of the movie
     */
    @Operation(summary = "Fetch the movie image URL by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movie image URL",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })
    })
    @GetMapping("/image/{id}")
    public String getMovieImageUrl(@PathVariable int id) {
        return movieApiClient.getMovieImageUrl(id);
    }

    /**
     * Fetch similar movies by movie ID
     *
     * @param id The ID of the movie
     * @return A map containing similar movie data
     */
    @Operation(summary = "Fetch similar movies by movie ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched similar movies",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })
    })
    @GetMapping("/similar/{id}")
    public Map<String, Object> getSimilarMovies(@PathVariable int id) {
        return movieApiClient.fetchSimilarMovies(id);
    }

    /**
     * Fetch recommended movies by movie ID
     *
     * @param id The ID of the movie
     * @return A map containing recommended movie data
     */
    @Operation(summary = "Fetch recommended movies by movie ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched recommended movies",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })
    })
    @GetMapping("/recommended/{id}")
    public Map<String, Object> getRecommendedMovies(@PathVariable int id) {
        return movieApiClient.fetchRecommendedMovies(id);
    }

    /**
     * Fetch the movie title by ID
     *
     * @param id The ID of the movie
     * @return The title of the movie
     */
    @Operation(summary = "Fetch the movie title by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movie title",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })
    })
    @GetMapping("/title/{id}")
    public String getMovieTitle(@PathVariable int id) {
        return movieApiClient.getMovieTitle(id);
    }

    /**
     * Fetch the movie overview by ID
     *
     * @param id The ID of the movie
     * @return The overview of the movie
     */
    @Operation(summary = "Fetch the movie overview by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movie overview",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })
    })
    @GetMapping("/overview/{id}")
    public String getMovieOverview(@PathVariable int id) {
        return movieApiClient.getMovieOverview(id);
    }

    /**
     * Fetch the movie release date by ID
     *
     * @param id The ID of the movie
     * @return The release date of the movie
     */
    @Operation(summary = "Fetch the movie release date by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movie release date",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })
    })
    @GetMapping("/release_date/{id}")
    public String getMovieReleaseDate(@PathVariable int id) {
        return movieApiClient.getMovieReleaseDate(id);
    }

    /**
     * Fetch the movie rating by ID
     *
     * @param id The ID of the movie
     * @return The rating of the movie
     */
    @Operation(summary = "Fetch the movie rating by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movie rating",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Double.class)) })
    })
    @GetMapping("/rating/{id}")
    public double getMovieRating(@PathVariable int id) {
        return movieApiClient.getMovieRating(id);
    }

    /**
     * Get Person id by Name
     *
     * @param name The name to search for
     * @return Person id
     */
    @Operation(summary = "Get Person id by Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched person id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })
    })
    @GetMapping("/personId/{name}")
    public int getPersonIdByName(@PathVariable String name) {
        return movieApiClient.getPersonIdByName(name);
    }

    /**
     * Get Keyword id by Name
     *
     * @param name The name to search for
     * @return Keyword id
     */
    @Operation(summary = "Get Keyword id by Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched keyword id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })
    })
    @GetMapping("/keywordId/{name}")
    public int getKeywordIdByName(@PathVariable String name) {
        return movieApiClient.getKeywordIdByName(name);
    }

    /**
     * Get Genre id by Name
     *
     * @param name The name to search for
     * @return Genre id
     */
    @Operation(summary = "Get Genre id by Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched genre id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })
    })
    @GetMapping("/genreId/{name}")
    public int getGenreIdByName(@PathVariable String name) {
        return movieApiClient.getGenreIdByName(name);
    }

    @Operation(summary = "Fetch the movie trailer URL by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movie trailer URL",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "Trailer not found",
                    content = @Content)
    })
    @GetMapping("/trailer/{id}")
    public ResponseEntity<String> getTrailerByMovieId(@PathVariable int id) {
        String trailerUrl = movieApiClient.getMovieTrailerUrl(id);

        if ("Trailer not available".equals(trailerUrl) || "Error fetching trailer".equals(trailerUrl)) {
            return ResponseEntity.status(404).body("Trailer not found for movie ID: " + id);
        }

        return ResponseEntity.ok(trailerUrl);
    }
    /**
     * Fetch a specific page of popular movies
     *
     * @param page The page number to fetch
     * @return A map containing popular movies data for the requested page
     */
    @Operation(summary = "Fetch a specific page of popular movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched popular movies data for the specified page",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })
    })
    @GetMapping("/popular/page/{page}")
    public Map<String, Object> getPopularMoviesByPage(@PathVariable int page) {
        return movieApiClient.fetchPopularMovies(page);
    }
}


