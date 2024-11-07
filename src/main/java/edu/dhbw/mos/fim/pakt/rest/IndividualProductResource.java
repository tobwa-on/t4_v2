/**
 *
 */
package edu.dhbw.mos.fim.pakt.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.dhbw.mos.fim.helper.ModelUtils;
import edu.dhbw.mos.fim.pakt.db.IndividualProductRepository;
import edu.dhbw.mos.fim.pakt.db.OrderRepository;
import edu.dhbw.mos.fim.pakt.model.IndividualProduct;
import edu.dhbw.mos.fim.pakt.model.Reading;
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
@Path("/indprod")
public class IndividualProductResource
{
	private static final Logger logger = LoggerFactory.getLogger(IndividualProductResource.class);

	@Inject
	private IndividualProductRepository repository;

	@Inject
	private OrderRepository orderRepository;

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<IndividualProduct> getAll()
	{
		return repository.listAll();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public IndividualProduct get(@PathParam("id") final Long id)
	{
		final var iprod = repository.findById(id);
		if (iprod == null)
		{
			throw new WebApplicationException(404);
		}
		return iprod;
	}

	@GET
	@Path("/lookup/{identifier}")
	@Produces(MediaType.APPLICATION_JSON)
	public IndividualProduct lookUp(@PathParam("identifier") final String identifier)
	{
		final var iprod = repository.findBySerialNumber(identifier);
		if (iprod == null)
		{
			throw new WebApplicationException(404);
		}
		return iprod;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	@Produces(MediaType.APPLICATION_JSON)
	public IndividualProduct add(final IndividualProduct iprod)
	{
		repository.persist(iprod);
		return iprod;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/assignToOrder/{id}/#{orderIdentifier}")
	@Transactional
	public IndividualProduct assignToOrder(@PathParam("id") final Long individualProductId,
			@PathParam("orderIdentifier") final String orderIdentifier)
	{
		final var prod = get(individualProductId);
		final var order = orderRepository.findByIdentifier(orderIdentifier);

		logger.info("Linking {} to order {}.", prod, order);

		if (prod != null && order != null)
		{
			prod.setOrder(order);
			order.getProducts().add(prod);
			orderRepository.flush();
			repository.flush();
		}
		else
			throw new WebApplicationException(404);

		return prod;
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void update(@PathParam("id") final Long id, final IndividualProduct updated)
	{
		final var iprod = get(id);

		if (iprod != null)
		{
			// do not copy read-only fields order & product
			ModelUtils.copyProperties(updated, iprod, "order", "product");
			repository.flush();
		}
	}

	@PUT
	@Path("/{id}/readings/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void updateReading(@PathParam("id") final Long id, @PathParam("name") final String name, final Reading reading)
	{
		final var iprod = get(id);

		if (iprod != null)
		{
			reading.setName(name);
			iprod.getReadings().put(reading);
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
