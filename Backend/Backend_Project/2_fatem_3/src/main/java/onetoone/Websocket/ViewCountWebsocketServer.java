package onetoone.Websocket;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import onetoone.MovieViewCount.MovieViewCount;
import onetoone.MovieViewCount.MovieViewCountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * WebSocket server for tracking movie view counts in real time.
 *
 * Endpoint: ws://localhost:8080/viewCount
 */
@ServerEndpoint("/viewCount/{movieId}")
@Component
public class ViewCountWebsocketServer {

    private static Map<Session, Integer> sessionMovieMap = new Hashtable<>();
    private static Map<Integer, Session> movieSessionMap = new Hashtable<>();

    // Server-side logger
    private final Logger logger = LoggerFactory.getLogger(ViewCountWebsocketServer.class);

    // Autowired repository for updating the movie view count in the database
    private static MovieViewCountRepository movieViewCountRepository;

    @Autowired
    public void setMovieViewCountRepository(MovieViewCountRepository repository) {
        movieViewCountRepository = repository;
    }

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("movieId") int movieId) throws IOException {
        // Increment the view count in the database
        incrementMovieViewCount(movieId);

        // Log the new connection and the movie ID
        logger.info("[onOpen] User connected for Movie ID: " + movieId);

        // Store session and movie ID mapping
        sessionMovieMap.put(session, movieId);
        movieSessionMap.put(movieId, session);

        // Send the updated view count to the client
        int currentViewCount = getMovieViewCount(movieId);
        session.getBasicRemote().sendText("Movie ID: " + movieId + ", Current View Count: " + currentViewCount);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
    }

    @OnClose
    public void onClose(Session session) throws IOException {
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    }

    private void incrementMovieViewCount(int movieId) {
        // Check if the movie ID exists in the database
        MovieViewCount movieViewCount;

        if (movieViewCountRepository.existsByApiID(movieId)) {
            // If the movie ID exists, fetch the current record
            movieViewCount = movieViewCountRepository.getReferenceByApiID(movieId);
            // Increment the existing view count
            movieViewCount.setViewCount(movieViewCount.getViewCount() + 1);
        } else {
            // If no record exists, create a new one with a view count of 1
            movieViewCount = new MovieViewCount(movieId, 1);
        }

        // Save the updated or new view count record to the database
        movieViewCountRepository.save(movieViewCount);
    }

    /**
     * Gets the current view count for a specific movie from the database.
     *
     * @param movieId the ID of the movie.
     * @return the current view count.
     */
    private int getMovieViewCount(int movieId) {
        // Check if the movie view count record exists
        if (movieViewCountRepository.existsByApiID(movieId)) {
            // Fetch the record and return the view count
            MovieViewCount movieViewCount = movieViewCountRepository.getReferenceByApiID(movieId);
            return movieViewCount.getViewCount();
        } else {
            // Return 0 if no record is found
            return 0;
        }
    }





}
