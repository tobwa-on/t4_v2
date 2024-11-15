package edu.dhbw.mos.fim.pakt.db;

import edu.dhbw.mos.fim.pakt.model.MovieStatus;
import edu.dhbw.mos.fim.usr.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class MovieStatusRepository extends BaseRepository<MovieStatus> {

    /**
     * Finds movie based on user ID and movie ID.
     *
     * @param user the user
     * @param movieId the movie ID
     * @return MovieStatus object if found, otherwise null
     */
    public MovieStatus findByUserIdAndMovieId(User user, Long movieId) {
        return find("uid = ?1 and movieId = ?2", user, movieId).firstResult();
    }

    /**
     * Finds all movies based on user ID and status.
     *
     * @param user the user
     * @param status the column to be requested
     * @return List of MovieStatus objects if found, otherwise null
     */
    public List<MovieStatus> findByUidAndStatus(User user, String status) {
        return list("uid = ?1 and " + status + " = true", user);
    }

    /**
     * Finds all movies based on user ID.
     *
     * @param user the user
     * @return List of MovieStatus objects if found, otherwise null
     */
    public List<MovieStatus> findByUserId(User user) {
        return list("uid", user);
    }

    @Override
    @Transactional
    public void persist(final MovieStatus movieStatus) {
        super.persist(movieStatus);
    }
}