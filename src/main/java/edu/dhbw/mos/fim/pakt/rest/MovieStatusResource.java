package edu.dhbw.mos.fim.pakt.rest;

import edu.dhbw.mos.fim.pakt.db.MovieStatusRepository;
import edu.dhbw.mos.fim.pakt.db.ReviewRepository;
import edu.dhbw.mos.fim.pakt.model.MovieStatus;
import edu.dhbw.mos.fim.pakt.model.Review;
import edu.dhbw.mos.fim.usr.db.UserRepository;
import edu.dhbw.mos.fim.usr.model.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/moviestatus")
public class MovieStatusResource extends BasicRestCrudResource<MovieStatus, MovieStatusRepository> {

    @Inject
    UserRepository userRepository;

    @Inject
    private MovieStatusRepository movieStatusRepository;

    @Inject
    private ReviewRepository reviewRepository;

    // Liefert eine Liste aller Film-IDs für einen bestimmten Benutzer und Status
    @GET
    @Path("/user={uid}/status={status}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMoviesByStatus(@PathParam("uid") String uid,
                                         @PathParam("status") String status) {

        User user = userRepository.findByUid(uid);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        List<MovieStatus> movies = movieStatusRepository.findByUidAndStatus(user, status);

        // KI
        List<Map<String, Object>> result = movies.stream()
                .map(movie -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("movieId", movie.getMovieId());
                    map.put("watchedAt", movie.getWatchedAt());
                    map.put("favoriteAt", movie.getFavoriteAt());
                    map.put("watchlistAt", movie.getWatchlistAt());
                    return map;
                })
                .toList();

        return Response.ok(result).build();

    }

    // Gibt den Filmstatus (Favorit, Watchlist, Gesehen) für einen Benutzer und Film zurück
    @GET
    @Path("/user={uid}/movie={movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieStatus(@PathParam("uid") String uid, @PathParam("movieId") Long movieId) {
        User user = userRepository.findByUid(uid);
        MovieStatus movieStatus = movieStatusRepository.findByUserIdAndMovieId(user, movieId);
        if (movieStatus == null) {
            return Response.status(Response.Status.NO_CONTENT).entity("Movie status not found").build();
        }
        return Response.ok()
                .entity(new StatusResponse(movieStatus.isFavorite(), movieStatus.isWatchlist(), movieStatus.isWatched()))
                .build();
    }

    // Aktualisiert den Filmstatus für einen Benutzer und Film, ggf. mit bestehendem Review
    @PUT
    @Path("/user/{uid}/movie/{movieId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateMovieStatus(@PathParam("uid") String uid, @PathParam("movieId") Long movieId, StatusUpdateRequest statusUpdateRequest) {
        User user = userRepository.findByUid(uid);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        MovieStatus movieStatus = movieStatusRepository.findByUserIdAndMovieId(user, movieId);
        Review existingReview = reviewRepository.findByUserIdAndMovieId(user, movieId);

        if (movieStatus == null) {
            movieStatus = new MovieStatus();
            movieStatus.setUid(user);
            movieStatus.setMovieId(movieId);
            if (existingReview != null) {
                movieStatus.setReview(existingReview);
            }
        }

        if (statusUpdateRequest.favorite != null) {
            movieStatus.setFavorite(statusUpdateRequest.favorite);
            if (statusUpdateRequest.favorite) {
                movieStatus.setFavoriteAt(LocalDateTime.now());  // Setzt `favoriteAt`, wenn `favorite` true ist
            } else {
                movieStatus.setFavoriteAt(null);  // Setzt `favoriteAt` auf null, wenn `favorite` false ist
            }
        }

        if (statusUpdateRequest.watchlist != null) {
            movieStatus.setWatchlist(statusUpdateRequest.watchlist);
            if (statusUpdateRequest.watchlist) {
                movieStatus.setWatchlistAt(LocalDateTime.now());  // Setzt `watchlistAt`, wenn `watchlist` true ist
            } else {
                movieStatus.setWatchlistAt(null);  // Setzt `watchlistAt` auf null, wenn `watchlist` false ist
            }
        }

        if (statusUpdateRequest.watched != null) {
            movieStatus.setWatched(statusUpdateRequest.watched);
            if (statusUpdateRequest.watched) {
                movieStatus.setWatchedAt(LocalDateTime.now());  // Setzt `watchedAt`, wenn `watched` true ist
            } else {
                movieStatus.setWatchedAt(null);  // Setzt `watchedAt` auf null, wenn `watched` false ist
            }
        }

        movieStatusRepository.persist(movieStatus);
        return Response.ok(new StatusResponse(movieStatus.isFavorite(), movieStatus.isWatchlist(), movieStatus.isWatched())).build();
    }


    // Antwortmodell für den Status-Rückgabewert
    public static class StatusResponse {
        public boolean favorite;
        public boolean watchlist;
        public boolean watched;

        public StatusResponse(boolean favorite, boolean watchlist, boolean watched) {
            this.favorite = favorite;
            this.watchlist = watchlist;
            this.watched = watched;
        }
    }

    // Anforderungsmodell für die Status-Aktualisierung
    public static class StatusUpdateRequest {
        public Boolean favorite;
        public Boolean watchlist;
        public Boolean watched;
    }

    // Löscht den Filmstatus-Eintrag für einen Benutzer und Film
    @DELETE
    @Path("/user={uid}/movie={movieId}")
    @Transactional
    public Response removeMovieStatus(@PathParam("uid") String uid, @PathParam("movieId") Long movieId) {
        User user = userRepository.findByUid(uid);
        MovieStatus movieStatus = movieStatusRepository.findByUserIdAndMovieId(user, movieId);
        if (movieStatus == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Movie status not found").build();
        }
        movieStatusRepository.delete(movieStatus);
        return Response.noContent().build();
    }
}
