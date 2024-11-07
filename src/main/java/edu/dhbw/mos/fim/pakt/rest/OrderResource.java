/**
 *
 */
package edu.dhbw.mos.fim.pakt.rest;

import java.util.List;

import edu.dhbw.mos.fim.helper.ModelUtils;
import edu.dhbw.mos.fim.pakt.db.OrderRepository;
import edu.dhbw.mos.fim.pakt.model.Order;
import jakarta.inject.Inject;
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
@Path("/order")
public class OrderResource
{
	@Inject
	private OrderRepository repository;

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<Order> getAll()
	{
		return repository.listAll();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Order get(@PathParam("id") final Long id)
	{
		final var entity = repository.findById(id);
		if (entity == null)
		{
			throw new WebApplicationException(404);
		}
		return entity;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	@Produces(MediaType.APPLICATION_JSON)
	public Order add(final Order entity)
	{
		repository.persist(entity);
		return entity;
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void update(@PathParam("id") final Long id, final Order updated)
	{
		final var entity = get(id);

		if (entity != null)
		{
			ModelUtils.copyProperties(updated, entity);
			repository.flush();
		}
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public void delete(@PathParam("id") final Long id)
	{
		if (!repository.deleteById(id))
			throw new WebApplicationException(404);
	}
}
