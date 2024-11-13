package edu.dhbw.mos.fim.pakt.model;

import edu.dhbw.mos.fim.usr.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class MovieStatus {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long movieId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid", nullable = false)
    private User uid;

    @Column(nullable = false)
    private boolean favorite = false;

    @Column(nullable = false)
    private boolean watchlist = false;

    @Column(nullable = false)
    private boolean watched = false;

    public MovieStatus() {}

    public MovieStatus(User uid, Long movieId, boolean favorite, boolean watchlist, boolean watched, Long reviewId) {
        this.uid = uid;
        this.movieId = movieId;
        this.favorite = favorite;
        this.watchlist = watchlist;
        this.watched = watched;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public User getUid() { return uid; }
    public void setUid(User uid) { this.uid = uid; }

    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public boolean isWatchlist() { return watchlist; }
    public void setWatchlist(boolean watchlist) { this.watchlist = watchlist; }

    public boolean isWatched() { return watched; }
    public void setWatched(boolean watched) { this.watched = watched; }
}