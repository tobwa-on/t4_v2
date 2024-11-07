/**
 * Created on 23.02.2024 by auch
 */
package edu.dhbw.mos.fim.pakt.graphql;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import edu.dhbw.mos.fim.pakt.db.CustomerRepository;
import edu.dhbw.mos.fim.pakt.model.Customer;
import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

/**
 *
 */
@GraphQLApi
@RequestScoped
public class CustomerResource extends BasicGraphqlCrudResource<Customer, CustomerRepository>
{

	@Override
	@Query("allCustomers")
	@Description("Get all customers")
	@Transactional
	public List<Customer> getAll()
	{
		return super.getAll();
	}

	@Override
	@Query("customer")
	@Description("Get customer by id")
	public Customer get(final Long id)
	{
		return super.get(id);
	}

	@Override
	@Mutation("addCustomer")
	@Description("adds a new customer")
	@Transactional
	public Customer add(final Customer entity)
	{
		return super.add(entity);
	}

	@Override
	@Mutation("updateCustomer")
	@Description("updates the given customer")
	@Transactional
	public Customer update(final Long id, final Customer updated)
	{
		return super.update(id, updated);
	}

	@Override
	@Mutation("deleteCustomer")
	@Description("deletes the given customer")
	@Transactional
	public boolean delete(final Long id)
	{
		return super.delete(id);
	}

	@Override
	@Subscription("customerCreatedOrUpdated")
	@Description("Stream of customer creation or update events")
	public Multi<Customer> createdOrUpdated()
	{
		return super.createdOrUpdated();
	}
}
