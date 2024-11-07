/**
 * Created on 25.02.2024 by auch
 */
package edu.dhbw.mos.fim.pakt.graphql;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import edu.dhbw.mos.fim.pakt.db.OrderRepository;
import edu.dhbw.mos.fim.pakt.model.Order;
import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

/**
 *
 */
@GraphQLApi
@RequestScoped
public class OrderResource extends BasicGraphqlCrudResource<Order, OrderRepository>
{

	@Override
	@Query("allOrders")
	@Description("Get all orders")
	@Transactional
	public List<Order> getAll()
	{
		return super.getAll();
	}

	@Override
	@Query("order")
	@Description("Get order by id")
	public Order get(final Long id)
	{
		return super.get(id);
	}

	@Override
	@Mutation("addOrder")
	@Description("creates a new order")
	@Transactional
	public Order add(final Order entity)
	{
		return super.add(entity);
	}

	@Override
	@Mutation("updateOrder")
	@Description("updates the given order")
	@Transactional
	public Order update(final Long id, final Order updated)
	{
		return super.update(id, updated);
	}

	@Override
	@Mutation("deleteOrder")
	@Description("deletes the given order")
	@Transactional
	public boolean delete(final Long id)
	{
		return super.delete(id);
	}

	@Override
	@Subscription("orderCreatedOrUpdated")
	@Description("Stream of order creation or update events")
	public Multi<Order> createdOrUpdated()
	{
		return super.createdOrUpdated();
	}
}
