/**
 * by auch
 */
package edu.dhbw.mos.fim.pakt.rest;

import edu.dhbw.mos.fim.pakt.db.ProductRepository;
import edu.dhbw.mos.fim.pakt.model.Product;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * @author auch
 *
 */
@Path("/products")
public class ProductResource extends BasicRestCrudResource<Product, ProductRepository>
{
	@Override
	@RolesAllowed("user")
	@Transactional
	public Product add(final Product product)
	{
		return super.add(product);
	}

	@Override
	@RolesAllowed("user")
	@Transactional
	public void update(@PathParam("id") final Long id, final Product updated)
	{
		super.update(id, updated);
	}

	@Override
	@RolesAllowed("user")
	@Transactional
	public void delete(@PathParam("id") final Long id)
	{
		super.delete(id);
	}
}
