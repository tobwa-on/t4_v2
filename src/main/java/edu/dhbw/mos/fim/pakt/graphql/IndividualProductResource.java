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

import edu.dhbw.mos.fim.pakt.db.IndividualProductRepository;
import edu.dhbw.mos.fim.pakt.model.IndividualProduct;
import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;

@GraphQLApi
@RequestScoped
public class IndividualProductResource extends BasicGraphqlCrudResource<IndividualProduct, IndividualProductRepository>
{
	@Override
	@Query("allIndividualProducts")
	@Description("Get all individual products")
	@Transactional
	public List<IndividualProduct> getAll()
	{
		return super.getAll();
	}

	@Override
	@Query("individualProduct")
	@Description("Get individual product by id")
	public IndividualProduct get(@Name("id") final Long id)
	{
		return super.get(id);
	}

	@Override
	// @Mutation("addIndividualProduct")
	@Description("adds a new individual product")
	@Transactional
	public IndividualProduct add(@Name("individualproduct") final IndividualProduct product)
	{
		return super.add(product);
	}

	@Override
	@Mutation("updateIndividualProduct")
	@Description("updates the given individual product")
	@Transactional
	public IndividualProduct update(@Name("id") final Long id, @Name("update") final IndividualProduct updated)
	{
		return super.update(id, updated);
	}

	@Override
	@Mutation("deleteIndividualProduct")
	@Description("deletes the given individual product")
	@Transactional
	public boolean delete(@Name("id") final Long id)
	{
		return super.delete(id);
	}

	@Override
	@Subscription("individualProductCreatedOrUpdated")
	@Description("Stream of indiv. product creation or update events")
	public Multi<IndividualProduct> createdOrUpdated()
	{
		return super.createdOrUpdated();
	}
}
