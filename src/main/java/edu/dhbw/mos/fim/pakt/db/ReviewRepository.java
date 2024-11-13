package edu.dhbw.mos.fim.pakt.db;

import edu.dhbw.mos.fim.pakt.model.Review;
import edu.dhbw.mos.fim.usr.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ReviewRepository extends BaseRepository<Review> {

    /**
     * Finds a review based on user ID and movie ID.
     *
     * @param user the user
     * @param movieId the movie ID
     * @return Review object if found, otherwise null
     */
    public Review findByUserIdAndMovieId(User user, Long movieId) {
        return find("uid = ?1 and movieId = ?2", user , movieId).firstResult();
    }

    /**
     * Finds all reviews for a specific movie ID.
     *
     * @param movieId the movie ID
     * @return List of Review objects for the movie
     */
    public List<Review> findByMovieId(Long movieId) {
        return list("movieId", movieId);
    }
}
