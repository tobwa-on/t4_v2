package edu.dhbw.mos.fim.pakt.rest;

import edu.dhbw.mos.fim.pakt.db.FavoriteRepository;
import edu.dhbw.mos.fim.pakt.model.Favorite;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/favorites")
public class FavoriteResource extends BasicRestCrudResource<Favorite, FavoriteRepository> {

    @Inject
    private FavoriteRepository favoriteRepository;

    @Override
    @Transactional
    public Favorite add(Favorite favorite) {
        Favorite existingFavorite = favoriteRepository.findByUserIdAndMovieId(favorite.getUid(), favorite.getMovieId());
        if (existingFavorite != null) {
            throw new WebApplicationException("Favorite already exists", Response.Status.CONFLICT);
        }

        return super.add(favorite);
    }

    @GET
    @Path("/user/{uid}/movie/{movieId}/exists")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isFavorite(@PathParam("uid") String uid, @PathParam("movieId") Long movieId) {
        Favorite favorite = favoriteRepository.findByUserIdAndMovieId(uid, movieId);
        boolean exists = (favorite != null);
        return Response.ok("{\"exists\":" + exists + "}").build();
    }

    @DELETE
    @Path("/user/{uid}/movie/{movieId}")
    @Transactional
    public Response removeFavorite(@PathParam("uid") String uid, @PathParam("movieId") Long movieId) {
        Favorite favorite = favoriteRepository.findByUserIdAndMovieId(uid, movieId);
        if (favorite == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Favorite not found").build();
        }
        favoriteRepository.delete(favorite);
        return Response.noContent().build();
    }

    @GET
    @Path("/user/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFavoritesByUserId(@PathParam("uid") String uid) {
        List<Favorite> favorites = favoriteRepository.findByUserId(uid);
        return Response.ok(favorites).build();
    }

}
