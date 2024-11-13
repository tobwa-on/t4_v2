package edu.dhbw.mos.fim.pakt.rest;

import edu.dhbw.mos.fim.pakt.db.ReviewRepository;
import edu.dhbw.mos.fim.pakt.model.Review;
import edu.dhbw.mos.fim.usr.db.UserRepository;
import edu.dhbw.mos.fim.usr.model.User;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
@Path("/reviews")
public class ReviewResource {

    @Inject
    UserRepository userRepository;
    @Inject
    private ReviewRepository reviewRepository;

    @POST
    @Path("/saveOrUpdate")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveOrUpdateReview(Review review) {
        Review existingReview = reviewRepository.findByUserIdAndMovieId(review.getUid(), review.getMovieId());

        if (existingReview != null) {
            existingReview.setRating(review.getRating());
            existingReview.setReviewText(review.getReviewText());
            reviewRepository.persist(existingReview);
            return Response.ok(existingReview).build();
        } else {
            reviewRepository.persist(review);
            return Response.ok(review).build();
        }
    }

    @GET
    @Path("/user/{uid}/movie/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewByUserAndMovie(@PathParam("uid") String uid, @PathParam("movieId") Long movieId) {
        User user = userRepository.findByUid(uid);

        Review review = reviewRepository.findByUserIdAndMovieId(user, movieId);
        if (review == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(review).build();
    }

    @GET
    @Path("/movie/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReviews(@PathParam("movieId") Long movieId) {
        List<Review> reviews = reviewRepository.findByMovieId(movieId);

        if (reviews == null || reviews.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(reviews).build();
    }
}