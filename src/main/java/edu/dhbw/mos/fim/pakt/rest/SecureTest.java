/**
 *
 */
package edu.dhbw.mos.fim.pakt.rest;

import java.util.Date;

import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

/**
 * author auch
 */
@Path("/securetest")
@RequestScoped
public class SecureTest
{
	@Inject
	JsonWebToken jwt;

	@GET
	@Path("/secured")
	@RolesAllowed("user")
	@Produces(MediaType.TEXT_PLAIN)
	public String test(@Context final SecurityContext ctx)
	{
		final String msg = String.format("Principal: %s, Issuer: %s, Expires: %s, Groups: %s, Token: %s",
				ctx.getUserPrincipal().getName(), jwt.getIssuer(), new Date(jwt.getExpirationTime() * 1000), jwt.getGroups(),
				jwt.toString());

		System.out.println(msg);

		return msg;
	}

	@GET
	@Path("/open")
	@PermitAll
	@Produces(MediaType.TEXT_PLAIN)
	public String open()
	{
		return "open.";
	}
}
