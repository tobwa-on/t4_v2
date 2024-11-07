package edu.dhbw.mos.fim.pakt.db;

import java.util.List;

import edu.dhbw.mos.fim.pakt.model.IndividualProduct;
import edu.dhbw.mos.fim.pakt.model.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@ApplicationScoped
public class IndividualProductRepository extends BaseRepository<IndividualProduct>
{
	@Inject
	private ProductRepository productRepository;

	@Inject
	private OrderRepository orderRepository;

	public IndividualProduct findBySerialNumber(final String serialNumber)
	{
		return find("serialNumber", serialNumber).firstResult();
	}

	public List<IndividualProduct> findAllFor(final Product product)
	{
		return find("product", product).list();
	}

	/**
	 * Update the given individual product's associations from the DB.
	 *
	 * @param iprod
	 */
	@Transactional(TxType.MANDATORY)
	public void updateAssociations(final IndividualProduct iprod)
	{
		final var product = iprod.getProduct();
		iprod.setProduct(productRepository.find(product));

		// order may be null
		final var order = iprod.getOrder();
		if (order != null)
		{
			final var dbOrder = orderRepository.find(order);
			iprod.setOrder(dbOrder);
		}
	}

	@Transactional
	public void mergeReadings(final IndividualProduct iprod)
	{
		final var dbProd = findById(iprod.getId());
		// TODO

		// dbProd.getReadings()
	}

	@Override
	@Transactional
	public void persist(final IndividualProduct entity)
	{
		updateAssociations(entity);
		super.persist(entity);
	}
}
