package edu.dhbw.mos.fim.pakt.db;

import edu.dhbw.mos.fim.pakt.model.Product;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository extends BaseRepository<Product>
{
	public Product findByName(final String productName)
	{
		return find("name", productName).firstResult();
	}

	public Product find(final Product product)
	{
		if (product == null)
			throw new IllegalArgumentException("product must not be null");

		final Long id = product.getId();
		return (id != null) ? findById(id) : findByName(product.getName());
	}
}
