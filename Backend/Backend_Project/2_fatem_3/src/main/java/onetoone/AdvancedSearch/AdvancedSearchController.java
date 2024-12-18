package onetoone.AdvancedSearch;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
@RestController
@RequestMapping("/AdvancedSearch")
public class AdvancedSearchController {
    @Autowired
    private AdvancedSearch AdvancedSearch;
    /**
     *  Advanced search movies
     *
     * @param advancedSearch Search parameters
     * @return A map containing movie search results
     */
    @Operation(summary = "Advanced search movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched advanced movie search results",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })
    })
    @PostMapping()
    public Map<String, Object> advancedSearchMovies(@RequestBody AdvancedSearch advancedSearch) {
        return AdvancedSearch.advancedSearchMovies(advancedSearch);
    }
}
