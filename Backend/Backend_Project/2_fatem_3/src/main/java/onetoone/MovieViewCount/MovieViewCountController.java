package onetoone.MovieViewCount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieViewCountController {

    @Autowired
    MovieViewCountRepository movieViewCountRepository;

    /**
     * Get all movie view counts
     *
     * @return A list of movie view count records
     */
    @Operation(summary = "Get all movie view counts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched all movie view counts",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)) })
    })
    @GetMapping(path = "/MovieViewCount")
    List<MovieViewCount> getAllMovieViewCount() {
        return movieViewCountRepository.findAll();
    }

    /**
     * Get movie view count by ID
     *
     * @param id The ID of the movie view count
     * @return The movie view count record, or an error record if not found
     */
    @Operation(summary = "Get movie view count by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movie view count by ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieViewCount.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie view count not found",
                    content = @Content)
    })
    @GetMapping(path = "/MovieViewCount/{id}")
    MovieViewCount getViewCountById(@PathVariable int id) {
        if(movieViewCountRepository.findById(id) == null){
            MovieViewCount notExist = new MovieViewCount();
            notExist.setId(-1);
            return notExist;
        }
        return movieViewCountRepository.findById(id);
    }

    /**
     * Get movie view count by movie ID
     *
     * @param movieId The ID of the movie
     * @return The movie view count, or a new record with default view count if not found
     */
    @Operation(summary = "Get movie view count by movie ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched movie view count by movie ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieViewCount.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie view count not found",
                    content = @Content)
    })
    @GetMapping(path = "/ViewCountById/{movieId}")
    MovieViewCount getViewCountByMovieId(@PathVariable int movieId) {
        if(movieViewCountRepository.findByApiID(movieId) == null){
            MovieViewCount notExist = new MovieViewCount();
            notExist.setViewCount(1);
            notExist.setApiId(movieId);
            return notExist;
        }
        return movieViewCountRepository.findByApiID(movieId);
    }

    /**
     * Create a new movie view count
     *
     * @param movieViewCount The movie view count record to create
     * @return A success message
     */
    @Operation(summary = "Create a new movie view count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created movie view count",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieViewCount.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content)
    })
    @PostMapping(path = "/MovieViewCount")
    String createUser(@RequestBody MovieViewCount movieViewCount) {
        if (movieViewCount == null)
            return "Failure";
        movieViewCountRepository.save(movieViewCount);
        return "Success";
    }

    /**
     * Update a movie view count by ID
     *
     * @param id The ID of the movie view count
     * @param request The new movie view count details
     * @return The updated movie view count record
     */
    @Operation(summary = "Update movie view count by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated movie view count",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieViewCount.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie view count not found",
                    content = @Content)
    })
    @PutMapping("/MovieViewCount/{id}")
    MovieViewCount updateMovieViewCount(@PathVariable int id, @RequestBody MovieViewCount request) {
        MovieViewCount movieViewCount = movieViewCountRepository.findById(id);
        if (movieViewCount == null)
            return null;
        movieViewCountRepository.save(request);
        return movieViewCountRepository.findById(id);
    }

    /**
     * Delete a movie view count by ID
     *
     * @param id The ID of the movie view count to delete
     * @return A success message
     */
    @Operation(summary = "Delete movie view count by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted movie view count",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })
    })
    @DeleteMapping(path = "/MovieViewCount/{id}")
    String deleteRole(@PathVariable int id) {
        movieViewCountRepository.deleteById(id);
        return "Success";
    }
}
