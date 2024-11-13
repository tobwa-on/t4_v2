package edu.dhbw.mos.fim.pakt.db;

import edu.dhbw.mos.fim.pakt.model.MovieStatus;
import edu.dhbw.mos.fim.usr.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class MovieStatusRepository extends BaseRepository<MovieStatus> {

    public MovieStatus findByUserIdAndMovieId(User user, Long movieId) {
        return find("uid = ?1 and movieId = ?2", user, movieId).firstResult();
    }

    public List<MovieStatus> findByUserId(User user) {
        return list("uid", user);
    }

    @Override
    @Transactional
    public void persist(final MovieStatus movieStatus) {
        super.persist(movieStatus);
    }
}