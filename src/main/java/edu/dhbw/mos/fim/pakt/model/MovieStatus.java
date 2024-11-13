package edu.dhbw.mos.fim.pakt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    @Column(length = 128, nullable = false)
    private String uid;

    @Column(nullable = false)
    private boolean favorite = false;

    @Column(nullable = false)
    private boolean watchlist = false;

    @Column(nullable = false)
    private boolean watched = false;

    public MovieStatus() {}

    public MovieStatus(String uid, Long movieId, boolean favorite, boolean watchlist, boolean watched, Long reviewId) {
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

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public boolean isWatchlist() { return watchlist; }
    public void setWatchlist(boolean watchlist) { this.watchlist = watchlist; }

    public boolean isWatched() { return watched; }
    public void setWatched(boolean watched) { this.watched = watched; }
}