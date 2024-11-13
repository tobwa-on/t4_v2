package edu.dhbw.mos.fim.pakt.rest;

import edu.dhbw.mos.fim.pakt.db.MovieStatusRepository;
import edu.dhbw.mos.fim.pakt.model.MovieStatus;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/moviestatus")
public class MovieStatusResource extends BasicRestCrudResource<MovieStatus, MovieStatusRepository> {

    @Inject
    private MovieStatusRepository movieStatusRepository;

    @Override
    @Transactional
    public MovieStatus add(MovieStatus movieStatus) {
        MovieStatus existingStatus = movieStatusRepository.findByUserIdAndMovieId(movieStatus.getUid(), movieStatus.getMovieId());
        if (existingStatus != null) {
            throw new WebApplicationException("Movie status already exists", Response.Status.CONFLICT);
        }

        return super.add(movieStatus);
    }

    @GET
    @Path("/user/{uid}/movie/{movieId}/exists")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isMovieStatus(@PathParam("uid") String uid, @PathParam("movieId") Long movieId) {
        MovieStatus movieStatus = movieStatusRepository.findByUserIdAndMovieId(uid, movieId);
        boolean exists = (movieStatus != null);
        return Response.ok("{\"exists\":" + exists + "}").build();
    }

    @DELETE
    @Path("/user/{uid}/movie/{movieId}")
    @Transactional
    public Response removeMovieStatus(@PathParam("uid") String uid, @PathParam("movieId") Long movieId) {
        MovieStatus movieStatus = movieStatusRepository.findByUserIdAndMovieId(uid, movieId);
        if (movieStatus == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Movie status not found").build();
        }
        movieStatusRepository.delete(movieStatus);
        return Response.noContent().build();
    }

    @GET
    @Path("/user/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieStatusesByUserId(@PathParam("uid") String uid) {
        List<MovieStatus> movieStatuses = movieStatusRepository.findByUserId(uid);
        return Response.ok(movieStatuses).build();
    }

    @GET
    @Path("/user/{uid}/movie/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieStatus(@PathParam("uid") String uid, @PathParam("movieId") Long movieId) {
        MovieStatus movieStatus = movieStatusRepository.findByUserIdAndMovieId(uid, movieId);

        if (movieStatus == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Movie status not found").build();
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
    public Response updateMovieStatus( @PathParam("uid") String uid, @PathParam("movieId") Long movieId,
            StatusUpdateRequest statusUpdateRequest) {

        try {
            // Überprüfen, ob der Eintrag bereits existiert
            MovieStatus movieStatus = movieStatusRepository.findByUserIdAndMovieId(uid, movieId);

            if (movieStatus == null) {
                // Neuen Eintrag erstellen
                movieStatus = new MovieStatus(uid, movieId,
                        statusUpdateRequest.favorite != null && statusUpdateRequest.favorite,
                        statusUpdateRequest.watchlist != null && statusUpdateRequest.watchlist,
                        statusUpdateRequest.watched != null && statusUpdateRequest.watched,
                        null);
                movieStatusRepository.persist(movieStatus);
            } else {
                // Statuswerte aktualisieren
                if (statusUpdateRequest.favorite != null) {
                    movieStatus.setFavorite(statusUpdateRequest.favorite);
                }
                if (statusUpdateRequest.watchlist != null) {
                    movieStatus.setWatchlist(statusUpdateRequest.watchlist);
                }
                if (statusUpdateRequest.watched != null) {
                    movieStatus.setWatched(statusUpdateRequest.watched);
                }
            }

            // Erfolgreiche Antwort zurückgeben
            return Response.ok(new StatusResponse(movieStatus.isFavorite(), movieStatus.isWatchlist(), movieStatus.isWatched())).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Fehler beim Aktualisieren des Filmstatus").build();
        }
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
