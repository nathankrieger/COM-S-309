package onetoone.Bookmarks;

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
@RequestMapping("/bookmarks")
public class BookmarksController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookmarksRepository bookmarksRepository;

    private String success = "Success!";
    private String failure = "Failure";

    /**
     * Get all bookmarks
     *
     * @return A list of all bookmarks
     */
    @Operation(summary = "Get all bookmarks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found bookmarks",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bookmark.class)) }),
            @ApiResponse(responseCode = "404", description = "No bookmarks found",
                    content = @Content)
    })
    @GetMapping
    public List<Bookmark> getAllBookmarks() {
        return bookmarksRepository.findAll();
    }

    /**
     * Get a bookmark by its ID
     *
     * @param id The ID of the bookmark
     * @return The bookmark with the specified ID
     */
    @Operation(summary = "Get a bookmark by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the bookmark",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bookmark.class)) }),
            @ApiResponse(responseCode = "404", description = "Bookmark not found",
                    content = @Content)
    })
    @GetMapping("/id/{id}")
    public Bookmark getBookmarkById(@PathVariable int id) {
        return bookmarksRepository.findById(id);
    }

    /**
     * Get a bookmark by name
     *
     * @param name The name of the bookmark
     * @return The bookmark with the specified name
     */
    @Operation(summary = "Get a bookmark by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the bookmark",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bookmark.class)) }),
            @ApiResponse(responseCode = "404", description = "Bookmark not found",
                    content = @Content)
    })
    @GetMapping("/name/{name}")
    public Bookmark getBookmarkByName(@PathVariable String name) {
        return bookmarksRepository.findByName(name);
    }

    /**
     * Get bookmarks by user ID
     *
     * @param userId The ID of the user
     * @return A list of bookmarks associated with the specified user
     */
    @Operation(summary = "Get bookmarks by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found bookmarks for the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bookmark.class)) }),
            @ApiResponse(responseCode = "404", description = "No bookmarks found for the user",
                    content = @Content)
    })
    @GetMapping("/userId/{userId}")
    public List<Bookmark> getBookmarksByUserId(@PathVariable int userId) {
        List<Bookmark> bookmarks = bookmarksRepository.findByUserId(userId);
        if (bookmarks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No bookmarks found for user with ID " + userId);
        }
        return bookmarks;
    }

    /**
     * Get bookmarks by user name
     *
     * @param userName The name of the user
     * @return A list of bookmarks associated with the specified user name
     */
    @Operation(summary = "Get bookmarks by user name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found bookmarks for the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bookmark.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found or no bookmarks found for the user",
                    content = @Content)
    })
    @GetMapping("/username/{userName}")
    public List<Bookmark> getBookmarksByUserName(@PathVariable String userName) {
        User user = userRepository.findByName(userName);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with name " + userName);
        }
        List<Bookmark> bookmarks = bookmarksRepository.findByUserId(user.getId());
        if (bookmarks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No bookmarks found for user with name " + userName);
        }
        return bookmarks;
    }

    /**
     * Get a bookmark by user ID and movie ID
     *
     * @param userId The ID of the user
     * @param movieId The ID of the movie
     * @return The bookmark if it exists, or an error bookmark with ID -1 if not found
     */
    @Operation(summary = "Get bookmark by user ID and movie ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the bookmark",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bookmark.class)) }),
            @ApiResponse(responseCode = "404", description = "Bookmark not found",
                    content = @Content)
    })
    @GetMapping("/userId/movieId/{userId}/{movieId}")
    public Bookmark getBookmarkByUserIdAndMovieId(@PathVariable int userId, @PathVariable int movieId) {
        Bookmark bookmark = bookmarksRepository.findByUserIdAndMovieId(userId, movieId);
        if (bookmark != null) {
            return bookmark;
        }

        // Return a bookmark with an error ID if not found
        Bookmark err = new Bookmark();
        err.setId(-1);
        return err;
    }

    /**
     * Create a new bookmark
     *
     * @param bookmark The bookmark object to be created
     * @param userId The ID of the user creating the bookmark
     * @param movieId The ID of the movie being bookmarked
     * @return A success message if the bookmark is created
     */
    @Operation(summary = "Create a new bookmark")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bookmark created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bookmark.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @PostMapping("/create/{userId}/{movieId}")
    public String createBookmark(@RequestBody Bookmark bookmark, @PathVariable int userId, @PathVariable int movieId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return "User not found"; // Handle error appropriately
        }
        bookmark.setUser(user); // Set the userId for the bookmark
        bookmarksRepository.save(bookmark);
        return success;
    }

    /**
     * Update an existing bookmark
     *
     * @param id The ID of the bookmark to be updated
     * @param request The updated bookmark data
     * @return The updated bookmark
     */
    @Operation(summary = "Update an existing bookmark")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookmark updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bookmark.class)) }),
            @ApiResponse(responseCode = "404", description = "Bookmark not found",
                    content = @Content)
    })
    @PutMapping("/update/{id}")
    public Bookmark updateBookmark(@PathVariable int id, @RequestBody Bookmark request) {
        Bookmark existingBookmark = bookmarksRepository.findById(id);
        if (existingBookmark == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bookmark not found with ID " + id);
        }

        // Update fields
        existingBookmark.setName(request.getName());
        existingBookmark.setMovieId(request.getMovieId());
        // Update other fields as needed

        bookmarksRepository.save(existingBookmark);
        return existingBookmark;
    }

    /**
     * Delete a bookmark by its ID
     *
     * @param id The ID of the bookmark to be deleted
     * @return A success message
     */
    @Operation(summary = "Delete a bookmark by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookmark deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Bookmark not found",
                    content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public String deleteBookmark(@PathVariable int id) {
        bookmarksRepository.deleteById(id);
        return success;
    }

    /**
     * Delete a bookmark by user ID and movie ID
     *
     * @param userId The ID of the user
     * @param movieId The ID of the movie
     * @return A success message
     */
    @Operation(summary = "Delete a bookmark by user ID and movie ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookmark deleted successfully",
                    content = @Content)
    })
    @DeleteMapping("/delete/{userId}/{movieId}")
    public String deleteBookmarkByUserIdAndMovieId(@PathVariable int userId, @PathVariable int movieId) {
        bookmarksRepository.deleteByUserIdAndMovieId(userId, movieId);
        return success;
    }
}
