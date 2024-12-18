package onetoone.Messages;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import onetoone.Bookmarks.Bookmark;
import onetoone.Comments.Comment;
import onetoone.Users.User;
import onetoone.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessagesController {

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all messages by user id
     *
     * @param userId The id of the user who sent the message
     * @return A list of all messages sent by specific user
     */
    @Operation(summary = "Get all Messages by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found messages",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "404", description = "No messages found",
                    content = @Content)
    })
    @GetMapping("/userId/{userId}")
    public List<Messages> getMessagesByUserId(@PathVariable int userId) {
        return messagesRepository.findByUserId(userId);
    }

    /**
     * Create new message
     *
     * @param message The message object to be created
     * @return The created message
     */
    @Operation(summary = "Create new message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @PostMapping("/create")
    public Messages createMessages(@RequestBody Messages message) {
        User user = userRepository.findById(message.getUser().getId());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID " + message.getUser().getId());
        }
        message.setUser(user);  // Associate the user with the comment
        return messagesRepository.save(message);
    }
}

