package onetoone;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.web.server.LocalServerPort;	// SBv3

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class WestinGjervoldSystemTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void getPersonIdByNameTest() {
        // Send request and receive response
        Response response = RestAssured.given().get("/api/movies/personId/Matt Damon");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("1892", returnString);
    }
    @Test
    public void getKeywordIdByNameTest() {
        // Send request and receive response
        Response response = RestAssured.given().get("/api/movies/keywordId/test");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("193415", returnString);
    }
    @Test
    public void getUserByIdTest() {
        // Send request and receive response
        Response response = RestAssured.given().get("/users/id/1");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("westin", returnObj.get("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getBookmarkByUserIdTest() {
        // Send request and receive response
        Response response = RestAssured.given().get("bookmarks/userId/1");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("Bookmark 1", returnObj.get("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMovieByIdTest() {
        // Send request and receive response
        Response response = RestAssured.given().get("/api/movies/1034541");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("Terrifier 3", returnObj.get("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}


