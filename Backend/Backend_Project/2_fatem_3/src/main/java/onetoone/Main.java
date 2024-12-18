package onetoone;


import onetoone.Movies.MovieApiClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import onetoone.Users.User;
import onetoone.Users.UserRepository;
import onetoone.Bookmarks.Bookmark;
import onetoone.Bookmarks.BookmarksRepository;
import org.springframework.beans.factory.annotation.Autowired;



@SpringBootApplication
@EnableJpaRepositories

public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Autowired
    private MovieApiClient movieApiClient;

    // Initialize User data
    @Bean
    CommandLineRunner initUser(UserRepository userRepository) {
        return args -> {
            createUserIfNotExists(userRepository, "westin", "westing@somemail.com", "westin");
            createUserIfNotExists(userRepository, "Jane", "jane@somemail.com", "password");
            createUserIfNotExists(userRepository, "Justin", "justin@somemail.com", "password");
        };
    }

    // Initialize Bookmark data
    @Bean
    CommandLineRunner initBookmarks(BookmarksRepository bookmarksRepository, UserRepository userRepository) {
        return args -> {
            User user = userRepository.findByName("westin"); // Get a user to associate with the bookmark
            createBookmarkIfNotExists(bookmarksRepository, "Bookmark 1", 101, user);
        };
    }

    // Create bookmarks if they don't exist
    private void createBookmarkIfNotExists(BookmarksRepository bookmarksRepository, String name, int movieId, User user) {
        if (!bookmarksRepository.existsByName(name)) {
            Bookmark bookmark = new Bookmark(name, movieId, user);
            bookmark.setUser(user); // Set the user for the bookmark
            bookmarksRepository.save(bookmark);
        }
    }

    // Create users if they don't exist
    private void createUserIfNotExists(UserRepository userRepository, String name, String email, String password) {
        if (!userRepository.existsByName(name)) {
            User user = new User(name, email, password);
            userRepository.save(user);
        }
    }

    // Run Movie API client and fetch movie details
    @Bean
    CommandLineRunner fetchMovieDetails() {
        return args -> {
            String movieDetails = movieApiClient.fetchMovieDetails(550).toString();  // Example movie ID for "Fight Club"

            if (movieDetails != null) {
                System.out.println("Movie Details: " + movieDetails);
            }
        };
    }





}
