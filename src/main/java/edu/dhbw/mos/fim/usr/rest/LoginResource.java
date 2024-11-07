/**
 *
 */
package edu.dhbw.mos.fim.usr.rest;

import java.time.Duration;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import edu.dhbw.mos.fim.pakt.rest.UserCredentials;
import io.quarkus.security.credential.PasswordCredential;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;

/**
 *
 */
@Path("/login")
@RequestScoped
public class LoginResource
{
	@ConfigProperty(name = "mp.jwt.verify.issuer")
	private String issuer;

	@Inject
	private IdentityProviderManager ipm;

	@POST
	@PermitAll
	public String login(final UserCredentials credentials)
	{
		final var upCred = new UsernamePasswordAuthenticationRequest(credentials.getUser(),
				new PasswordCredential(credentials.getPassword().toCharArray()));

		final Optional<SecurityIdentity> resultOpt = ipm.authenticate(upCred).await().asOptional().indefinitely();

		if (resultOpt.isPresent())
		{
			final SecurityIdentity identity = resultOpt.get();

			return Jwt.issuer(issuer).upn(identity.getPrincipal().getName()).groups(identity.getRoles())
					.expiresIn(Duration.ofHours(2)).sign();
		}
		else
			throw new WebApplicationException(405);
	}
}
