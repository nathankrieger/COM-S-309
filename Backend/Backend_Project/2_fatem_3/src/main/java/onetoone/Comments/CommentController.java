package onetoone.Comments;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import onetoone.Users.User;
import onetoone.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get comments by movie ID
     *
     * @param movieId The ID of the movie
     * @return A list of comments associated with the specified movie
     */
    @Operation(summary = "Get comments by movie ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found comments for the movie",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid movie ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Comments not found",
                    content = @Content)
    })
    @GetMapping("/movie/{movieId}")
    public List<Comment> getCommentsByMovieId(@PathVariable int movieId) {
        return commentRepository.findByMovieId(movieId);
    }

    /**
     * Get comments by user ID
     *
     * @param userId The ID of the user
     * @return A list of comments made by the specified user
     */
    @Operation(summary = "Get comments by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found comments for the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid user ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Comments not found",
                    content = @Content)
    })
    @GetMapping("/user/{userId}")
    public List<Comment> getCommentsByUserId(@PathVariable int userId) {
        return commentRepository.findByUserId(userId);
    }

    /**
     * Create a new comment
     *
     * @param comment The comment object to be created
     * @return The created comment
     */
    @Operation(summary = "Create a new comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @PostMapping("/create")
    public Comment createComment(@RequestBody Comment comment) {
        User user = userRepository.findById(comment.getUserid());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID " + comment.getUserid());
        }
        comment.setUser(user);  // Associate the user with the comment
        comment.setMovieId(comment.getMovieId()); // Ensure movieId is set
        return commentRepository.save(comment);
    }
}
