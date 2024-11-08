package edu.dhbw.mos.fim.pakt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Favorite {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(length = 128, nullable = false)
    private String uid;  // Benutzer-ID

    @NotNull
    @Column(nullable = false)
    private Long movieId;  // ID des Films

    public Favorite() {}

    public Favorite(String uid, Long movieId) {
        this.uid = uid;
        this.movieId = movieId;
    }

    // Getter und Setter für uid und movieId
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
