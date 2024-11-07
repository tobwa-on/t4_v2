package edu.dhbw.mos.fim.pakt.db;

import edu.dhbw.mos.fim.pakt.model.Customer;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author auch
 *
 */
@ApplicationScoped
public class CustomerRepository extends BaseRepository<Customer>
{
	public Customer find(final Customer entity)
	{
		if (entity == null)
			throw new IllegalArgumentException("entity must not be null");

		final Long id = entity.getId();
		return findById(id);
	}
}
