package edu.dhbw.mos.fim.pakt.db;

import edu.dhbw.mos.fim.pakt.model.Review;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ReviewRepository extends BaseRepository<Review> {

    /**
     * Finds a review based on user ID and movie ID.
     *
     * @param uid the user ID
     * @param movieId the movie ID
     * @return Review object if found, otherwise null
     */
    public Review findByUserIdAndMovieId(String uid, Long movieId) {
        return find("uid = ?1 and movieId = ?2", uid, movieId).firstResult();
    }

    /**
     * Finds all reviews for a specific user ID.
     *
     * @param uid the user ID
     * @return List of Review objects for the user
     */
    public List<Review> findByUserId(String uid) {
        return list("uid", uid);
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

    /**
     * Updates the rating and review text of an existing review.
     *
     * @param uid the user ID
     * @param movieId the movie ID
     * @param newRating the new rating
     * @param newReviewText the new review text
     */
    @Transactional
    public void updateReview(String uid, Long movieId, Double newRating, String newReviewText) {
        Review existingReview = findByUserIdAndMovieId(uid, movieId);
        if (existingReview != null) {
            existingReview.setRating(newRating);
            existingReview.setReviewText(newReviewText);
            persist(existingReview);
        }
    }

    /**
     * Deletes a review based on user ID and movie ID.
     *
     * @param uid the user ID
     * @param movieId the movie ID
     */
    @Transactional
    public void deleteByUserIdAndMovieId(String uid, Long movieId) {
        Review existingReview = findByUserIdAndMovieId(uid, movieId);
        if (existingReview != null) {
            delete(existingReview);
        }
    }
}
