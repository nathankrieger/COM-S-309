package onetoone.Users;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import onetoone.Messages.Messages;
import onetoone.Roles.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    onetoone.Users.UserRepository userRepository;

    private String success = "Success!";
    private String failure = "Failure";

    /**
     * Get all users
     *
     * @return A list of all users
     */
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "No users found",
                    content = @Content)
    })
    @GetMapping()
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by user id
     *
     * @param id The id of the user
     * @return The user with the specific id
     */
    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "No user found",
                    content = @Content)
    })
    @GetMapping(path = "/id/{id}")
    User getUserById(@PathVariable int id) {
        if(userRepository.findById(id) == null){
            User notExist = new User();
            notExist.setId(-1);
            return notExist;
        }
        return userRepository.findById(id);
    }

    /**
     * Get user by name
     *
     * @param name The name of the user
     * @return The user with the specific name
     */
    @Operation(summary = "Get user by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "No user found",
                    content = @Content)
    })
    @GetMapping(path = "/name/{name}")
    User getUserByName(@PathVariable String name) {
        if(userRepository.findByName(name) == null){
            User notExist = new User();
            notExist.setId(-1);
            return notExist;
        }
        return userRepository.findByName(name);
    }

    /**
     * Create new user
     *
     * @param user The user object to be created
     * @return The created user
     */
    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content)
    })
    @PostMapping(path = "/create")
    public String createUser(@RequestBody User user) {
        if (user == null) return "Failure";

        if (userRepository.existsByName(user.getName())) {
            return "Username already exists, please choose another username.";
        }

        // Assign default role if no role is explicitly provided
        if (user.getRole() == null) {
            Roles defaultRole = new Roles("User", user);
            user.setRole(defaultRole);
        }

        user.setIfActive(true);
        userRepository.save(user);
        return "Success!";
    }

    /**
     * Login the user
     *
     * @param request The user object to be logged in
     * @return The user logged in
     */
    @Operation(summary = "Login the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User logged in successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @PostMapping(path = "/login")
    User login(@RequestBody User request) {
        String name = request.getName();
        String password = request.getPassword();
        User user = userRepository.findByNameAndPassword(name, password);
        if (user == null){
            User notExist = new User();
            notExist.setId(-1);
            return notExist;
        }
        user.setIfActive(true);
        userRepository.save(user);
        return user;
    }

    /**
     * Logout the user
     *
     * @param id The userId of the user to be logged out
     * @return A success message
     */
    @Operation(summary = "Logout the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User logged out successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @PostMapping("/logout/{id}")
    public String logoutUser(@PathVariable int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return failure;
        }

        user.setIfActive(false);
        userRepository.save(user);
        return success;
    }

    /**
     * Update an existing user
     *
     * @param id The ID of the user to be updated
     * @param request The updated user data
     * @return The updated user
     */
    @Operation(summary = "Update an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @PutMapping("/update/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request) {
        User user = userRepository.findById(id);
        if (user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @PutMapping("/roles/update/{id}")
    public String updateUserRole(@PathVariable int id, @RequestBody String newRoleName) {
        User user = userRepository.findById(id);
        if (user == null) {
            return "User not found!";
        }

        // Clean the role name (sanitize input)
        newRoleName = newRoleName.replace("\"", "").trim();

        // Update or create the user's role
        Roles updatedRole = user.getRole();
        if (updatedRole == null) {
            updatedRole = new Roles(newRoleName, user);
        } else {
            updatedRole.setRoleName(newRoleName);
        }

        user.setRole(updatedRole);
        userRepository.save(user);
        return "Role updated successfully!";
    }

    /**
     * Update an existing users password
     *
     * @param request The updated user data
     * @return The updated user
     */
    @Operation(summary = "Update an existing users password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users password updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @PutMapping("/ForgotPassword")
    User forgotPassword(@RequestBody User request) {
        String name = request.getName();
        User user = userRepository.findByName(name);
        if (user == null) {
            User notExist = new User();
            notExist.setId(-1);
            return notExist;
        }
        user.setPassword(request.getPassword());
        userRepository.save(user);
        return userRepository.findByName(name);
    }

    /**
     * Delete a user by its ID
     *
     * @param id The ID of the user to be deleted
     * @return A success message
     */
    @Operation(summary = "Delete a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @DeleteMapping(path = "/delete/{id}")
    String deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return success;
    }

}
