package edu.dhbw.mos.fim.usr.rest;

import edu.dhbw.mos.fim.usr.db.UserRepository;
import edu.dhbw.mos.fim.usr.model.Role;
import edu.dhbw.mos.fim.usr.model.User;
import io.quarkus.elytron.security.common.BcryptUtil;
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


    @POST
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response changePassword(
            @FormParam("uid") String uid,
            @FormParam("currentPassword") String currentPassword,
            @FormParam("newPassword") String newPassword) {

        User user = userRepository.findByUid(uid);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Benutzer nicht gefunden.")
                    .build();
        }

        if (!BcryptUtil.matches(currentPassword, user.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Aktuelles Passwort ist falsch.")
                    .build();
        }

        user.setPassword(BcryptUtil.bcryptHash(newPassword));
        userRepository.persist(user);

        return Response.ok("Passwort erfolgreich geändert.").build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response registerUser(
            @FormParam("username") String username,
            @FormParam("password") String password) {

        // Überprüfen, ob der Benutzername bereits existiert
        if (userRepository.findByUid(username) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Benutzername bereits vergeben.")
                    .build();
        }

        // Neues Passwort hashen und neuen Benutzer erstellen
        User newUser = new User();
        newUser.setUid(username);
        newUser.setPassword(BcryptUtil.bcryptHash(password));
        userRepository.persist(newUser);

        return Response.status(Response.Status.CREATED)
                .entity("Benutzer erfolgreich registriert.")
                .build();
    }
}
