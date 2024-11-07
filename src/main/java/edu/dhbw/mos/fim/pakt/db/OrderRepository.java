/**
 *
 */
package edu.dhbw.mos.fim.pakt.db;

import edu.dhbw.mos.fim.pakt.model.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * @author auch
 *
 */
@ApplicationScoped
public class OrderRepository extends BaseRepository<Order>
{
	@Inject
	private CustomerRepository customerRepository;

	@Inject
	private ProductRepository productRepository;

	public Order find(final Order order)
	{
		if (order == null)
			throw new IllegalArgumentException("order must not be null");

		final long id = order.getId();
		return findById(id);
	}

	/**
	 * Tries to find the matching order by first looking for a matching id, then
	 * for a matching externalId.
	 *
	 * @param identifier
	 *          id or externalId
	 * @return Order or null if none found
	 */
	public Order findByIdentifier(final String identifier)
	{
		Order order = null;

		try
		{
			final long id = Long.parseLong(identifier);
			order = findById(id);
		}
		catch (final NumberFormatException e)
		{
			// ignore
		}

		if (order == null)
		{
			order = find("externalId", identifier).firstResult();
		}

		return order;
	}

	@Override
	@Transactional
	public void persist(final Order entity)
	{
		updateAssociations(entity);
		super.persist(entity);
	}

	private void updateAssociations(final Order entity)
	{
		final var customer = entity.getCustomer();
		entity.setCustomer(customerRepository.find(customer));

		for (final var iprod : entity.getProducts())
		{
			iprod.setProduct(productRepository.find(iprod.getProduct()));
			iprod.setOrder(entity);
		}
	}
}
