package edu.dhbw.mos.fim.usr.rest;

import edu.dhbw.mos.fim.usr.db.UserRepository;
import edu.dhbw.mos.fim.usr.model.Role;
import edu.dhbw.mos.fim.usr.model.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;
import java.util.stream.Collectors;

@Path("/user")
@RequestScoped
public class UserResource {

    @Inject
    UserRepository userRepository;

    /**
     * API Endpoint to retrieve roles of a user based on their uid.
     *
     * @param uid the unique identifier of the user
     * @return a set of role names assigned to the user
     */
    @GET
    @Path("/roles")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRoles(@QueryParam("uid") String uid) {
        User user = userRepository.findByUid(uid);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found with uid: " + uid)
                    .build();
        }

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return Response.ok(roleNames).build();
    }
}
