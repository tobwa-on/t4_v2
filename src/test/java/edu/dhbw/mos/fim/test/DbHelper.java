package edu.dhbw.mos.fim.test;

import edu.dhbw.mos.fim.pakt.db.CustomerRepository;
import edu.dhbw.mos.fim.pakt.db.IndividualProductRepository;
import edu.dhbw.mos.fim.pakt.db.OrderRepository;
import edu.dhbw.mos.fim.pakt.db.ProductRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Dependent
public class DbHelper
{
	@Inject
	private ProductRepository productRepository;

	@Inject
	private IndividualProductRepository individualProductRepository;

	@Inject
	private CustomerRepository customerRepository;

	@Inject
	private OrderRepository orderRepository;

	@Transactional
	public void clearDb()
	{
		individualProductRepository.deleteAll();
		orderRepository.deleteAll();
		productRepository.deleteAll();
		customerRepository.deleteAll();
	}
}
