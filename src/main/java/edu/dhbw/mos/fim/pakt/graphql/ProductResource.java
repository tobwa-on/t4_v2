/**
 * by auch
 */
package edu.dhbw.mos.fim.pakt.graphql;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import edu.dhbw.mos.fim.pakt.db.ProductRepository;
import edu.dhbw.mos.fim.pakt.model.Product;
import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

@GraphQLApi
@RequestScoped
public class ProductResource extends BasicGraphqlCrudResource<Product, ProductRepository>
{
	@Override
	@Query("allProducts")
	@Description("Get all products")
	@Transactional
	public List<Product> getAll()
	{
		return super.getAll();
	}

	@Override
	@Query("product")
	@Description("Get product by id")
	public Product get(@Name("id") final Long id)
	{
		return super.get(id);
	}

	@Override
	@Mutation("addProduct")
	@Description("adds a new product")
	@Transactional
	public Product add(@Name("product") final Product product)
	{
		return super.add(product);
	}

	@Override
	@Mutation("updateProduct")
	@Description("updates the given product")
	@Transactional
	public Product update(@Name("id") final Long id, @Name("update") final Product updated)
	{
		return super.update(id, updated);
	}

	@Override
	@Mutation("deleteProduct")
	@Description("deletes the given product")
	@Transactional
	public boolean delete(@Name("id") final Long id)
	{
		return super.delete(id);
	}

	@Override
	@Subscription("productCreatedOrUpdated")
	@Description("Stream of product creation or update events")
	public Multi<Product> createdOrUpdated()
	{
		return super.createdOrUpdated();
	}
}
