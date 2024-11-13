package edu.dhbw.mos.fim.pakt.rest;

import edu.dhbw.mos.fim.pakt.db.MovieStatusRepository;
import edu.dhbw.mos.fim.pakt.model.MovieStatus;
import edu.dhbw.mos.fim.usr.db.UserRepository;
import edu.dhbw.mos.fim.usr.model.User;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/moviestatus")
public class MovieStatusResource extends BasicRestCrudResource<MovieStatus, MovieStatusRepository> {

    @Inject
    UserRepository userRepository;
    @Inject
    private MovieStatusRepository movieStatusRepository;

    @DELETE
    @Path("/user/{uid}/movie/{movieId}")
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

    @GET
    @Path("/user={uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieStatusesByUserId(@PathParam("uid") String uid) {
        User user = userRepository.findByUid(uid);
        List<MovieStatus> movieStatuses = movieStatusRepository.findByUserId(user);
        return Response.ok(movieStatuses).build();
    }

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

        if (movieStatus == null) {
            movieStatus = new MovieStatus();
            movieStatus.setUid(user);
            movieStatus.setMovieId(movieId);
        }

        // Update or initialize the status fields
        if (statusUpdateRequest.favorite != null) {
            movieStatus.setFavorite(statusUpdateRequest.favorite);
        }
        if (statusUpdateRequest.watchlist != null) {
            movieStatus.setWatchlist(statusUpdateRequest.watchlist);
        }
        if (statusUpdateRequest.watched != null) {
            movieStatus.setWatched(statusUpdateRequest.watched);
        }

        movieStatusRepository.persist(movieStatus);
        return Response.ok(new StatusResponse(movieStatus.isFavorite(), movieStatus.isWatchlist(), movieStatus.isWatched())).build();
    }


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

    public static class StatusUpdateRequest {
        public Boolean favorite;
        public Boolean watchlist;
        public Boolean watched;
    }
}
