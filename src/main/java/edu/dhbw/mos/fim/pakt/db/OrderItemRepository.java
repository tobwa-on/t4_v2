/**
 *
 */
package edu.dhbw.mos.fim.pakt.db;

import edu.dhbw.mos.fim.pakt.model.OrderItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author auch
 *
 */
@ApplicationScoped
public class OrderItemRepository implements PanacheRepository<OrderItem>
{
	public OrderItem find(final OrderItem orderItem)
	{
		if (orderItem == null)
			throw new IllegalArgumentException("product must not be null");

		final long id = orderItem.getId();
		return findById(id);
	}
}
