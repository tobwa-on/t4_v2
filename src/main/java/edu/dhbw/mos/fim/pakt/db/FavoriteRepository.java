package edu.dhbw.mos.fim.pakt.db;

import edu.dhbw.mos.fim.pakt.model.Favorite;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class FavoriteRepository extends BaseRepository<Favorite> {

    /**
     * Finds a favorite based on user ID and movie ID.
     *
     * @param uid the user ID
     * @param movieId the movie ID
     * @return Favorite object if found, otherwise null
     */
    public Favorite findByUserIdAndMovieId(String uid, Long movieId) {
        return find("uid = ?1 and movieId = ?2", uid, movieId).firstResult();
    }

    /**
     * Finds all favorites for a specific user ID.
     *
     * @param uid the user ID
     * @return List of Favorite objects for the user
     */
    public List<Favorite> findByUserId(String uid) {
        return list("uid", uid);
    }

    @Override
    @Transactional
    public void persist(final Favorite favorite) {
        super.persist(favorite);
    }
}