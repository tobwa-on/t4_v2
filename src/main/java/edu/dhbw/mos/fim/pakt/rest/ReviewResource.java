package edu.dhbw.mos.fim.pakt.rest;

import edu.dhbw.mos.fim.pakt.db.ReviewRepository;
import edu.dhbw.mos.fim.pakt.model.Review;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reviews")
public class ReviewResource {

    @Inject
    private ReviewRepository reviewRepository;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReview(Review review) {
        reviewRepository.persist(review);
        return Response.ok(review).build();
    }

    @GET
    @Path("/user/{uid}/movie/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewByUserAndMovie(@PathParam("uid") String uid, @PathParam("movieId") Long movieId) {
        Review review = reviewRepository.findByUserIdAndMovieId(uid, movieId);
        if (review == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(review).build();
    }
    
    @PUT
    @Path("/user/{uid}/movie/{movieId}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReview(
            @PathParam("uid") String uid,
            @PathParam("movieId") Long movieId,
            Review updatedReview) {
        Review existingReview = reviewRepository.findByUserIdAndMovieId(uid, movieId);
        if (existingReview == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Review not found").build();
        }

        existingReview.setRating(updatedReview.getRating());
        existingReview.setReviewText(updatedReview.getReviewText());
        reviewRepository.persist(existingReview);

        return Response.ok(existingReview).build();
    }

}
