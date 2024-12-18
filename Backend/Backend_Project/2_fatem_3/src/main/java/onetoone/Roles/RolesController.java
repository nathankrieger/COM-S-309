package onetoone.Roles;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import onetoone.Users.User;
import onetoone.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    private UserRepository userRepository;


    /**
     * Get all roles
     *
     * @return A list of all roles
     */
    @Operation(summary = "Get all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found roles",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Roles.class)) }),
            @ApiResponse(responseCode = "404", description = "No bookmarks found",
                    content = @Content)
    })
    @GetMapping
    List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }


    @Operation(summary = "Create a role and link it to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created and linked successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Roles.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @PostMapping(path = "/create")
    String createRole(@RequestBody Roles role) {
        User user = userRepository.findById(role.getUser().getId());
        if (user == null) {
            return "User not found";
        }
        if (user.getRole() == null) {
            Roles defaultRole = new Roles("User", user);
            user.setRole(defaultRole);
        }
        rolesRepository.save(role);
        return "Role created successfully";
    }

    @Operation(summary = "Get role by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the role",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Roles.class)) }),
            @ApiResponse(responseCode = "404", description = "Role not found",
                    content = @Content)
    })
    @GetMapping(path = "/user/{userId}")
    Roles getRoleByUserId(@PathVariable int userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return null;
        }
        return rolesRepository.findByUser(user);
    }
    /**
     * Delete a role by its ID
     *
     * @param id The ID of the role to be deleted
     * @return A success message
     */
    @Operation(summary = "Delete a role by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Role not found",
                    content = @Content)
    })
    @DeleteMapping(path = "/delete/{id}")
    String deleteRole(@PathVariable int id) {
        rolesRepository.deleteById(id);
        return "Success";
    }

}
