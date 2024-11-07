/**
 * by auch
 */
package edu.dhbw.mos.fim.pakt.rest;

import java.util.List;

import edu.dhbw.mos.fim.pakt.db.BaseRepository;
import edu.dhbw.mos.fim.pakt.db.RepositoryAware;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;

/**
 * @author auch
 *
 */
class BasicRestCrudResource<E, BR extends BaseRepository<E>> extends RepositoryAware<E, BR>
{
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<E> getAll()
	{
		return getRepository().listAll();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public E get(@PathParam("id") final Long id)
	{
		final var entity = getRepository().findById(id);
		if (entity == null)
		{
			throw new WebApplicationException(404);
		}
		return entity;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public E add(final E entity)
	{
		getRepository().persist(entity);
		return entity;
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void update(@PathParam("id") final Long id, final E updated)
	{
		getRepository().update(id, updated);
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public void delete(@PathParam("id") final Long id)
	{
		if (!getRepository().deleteById(id))
			throw new WebApplicationException(404);
	}
}
