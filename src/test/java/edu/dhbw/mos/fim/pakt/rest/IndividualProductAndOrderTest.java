/**
 * Created on 25.06.2024 by auch
 */
package edu.dhbw.mos.fim.pakt.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.github.javafaker.Faker;

import edu.dhbw.mos.fim.pakt.model.Customer;
import edu.dhbw.mos.fim.pakt.model.IndividualProduct;
import edu.dhbw.mos.fim.pakt.model.Order;
import edu.dhbw.mos.fim.pakt.model.Product;
import edu.dhbw.mos.fim.test.DbHelper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;

/**
 *
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@TestInstance(Lifecycle.PER_CLASS)
public class IndividualProductAndOrderTest
{
	private static final String INDPROD_SERNO = "abcdefgh";
	private static final String ORDER_EXTID = "123AEC000";

	@Inject
	private DbHelper dbHelper;

	@Inject
	private ProductResource productResource;

	@Inject
	private OrderResource orderResource;

	@Inject
	private IndividualProductResource individualProductResource;

	@Inject
	private CustomerResource customerResource;

	private final Faker faker = Faker.instance();

	@AfterAll
	@BeforeAll
	public void clearDb()
	{
		dbHelper.clearDb();
	}

	private Product product;
	private IndividualProduct individualProduct;

	@Test
	@TestSecurity(user = "user", roles = { "admin", "user" })
	public void test01AddProduct()
	{
		product = productResource.add(new Product("Testprodukt"));

		assertEquals(product, productResource.get(product.getId()));
	}

	@Test
	@TestSecurity(user = "user", roles = { "admin", "user" })
	public void test10AddIndividualProduct()
	{
		individualProduct = individualProductResource.add(new IndividualProduct(INDPROD_SERNO, product));

		assertEquals(individualProduct, individualProductResource.get(individualProduct.getId()));
	}

	@Test
	@TestSecurity(user = "user", roles = { "admin", "user" })
	public void test20AddOrder()
	{
		final var customer = new Customer(faker.name().firstName(), faker.name().lastName(),
				faker.internet().emailAddress());
		customerResource.add(customer);

		var order = new Order(customer, ORDER_EXTID);
		orderResource.add(order);

		final var indprod = individualProductResource.lookUp(INDPROD_SERNO);
		assertNotNull(indprod);

		assertEquals(ORDER_EXTID,
				individualProductResource.assignToOrder(indprod.getId(), ORDER_EXTID).getOrder().getExternalId());

		// refresh order
		order = orderResource.get(order.getId());
		System.out.println(order);
		assertEquals(customer, order.getCustomer());
		assertEquals(1, order.getProducts().size());
		assertEquals(indprod, order.getProducts().stream().findAny().get());
	}
}
