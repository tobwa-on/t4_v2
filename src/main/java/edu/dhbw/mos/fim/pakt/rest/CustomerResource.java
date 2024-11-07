/**
 * by auch
 */
package edu.dhbw.mos.fim.pakt.rest;

import java.util.List;

import edu.dhbw.mos.fim.pakt.db.CustomerRepository;
import edu.dhbw.mos.fim.pakt.model.Customer;
import io.quarkus.panache.common.Sort;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * @author auch
 *
 */
@Path("/customer")
public class CustomerResource extends BasicRestCrudResource<Customer, CustomerRepository>
{
	private static final Sort DEFAULT_SORT_ORDER = Sort.ascending("familyName", "givenName");

	@Override
	@Transactional
	@PermitAll
	public List<Customer> getAll()
	{
		return getRepository().listAll(DEFAULT_SORT_ORDER);
	}

	@Override
	@Transactional
	@RolesAllowed("admin")
	public Customer add(final Customer customer)
	{
		return super.add(customer);
	}

	@Override
	@Transactional
	@RolesAllowed("admin")
	public void update(@PathParam("id") final Long customerId, final Customer updated)
	{
		super.update(customerId, updated);
	}

	@Override
	@Transactional
	@RolesAllowed("admin")
	public void delete(@PathParam("id") final Long customerId)
	{
		super.delete(customerId);
	}
}
