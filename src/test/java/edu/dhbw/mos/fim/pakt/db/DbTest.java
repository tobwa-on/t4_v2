package edu.dhbw.mos.fim.pakt.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;

import edu.dhbw.mos.fim.pakt.db.CustomerRepository;
import edu.dhbw.mos.fim.pakt.db.IndividualProductRepository;
import edu.dhbw.mos.fim.pakt.db.OrderRepository;
import edu.dhbw.mos.fim.pakt.db.ProductRepository;
import edu.dhbw.mos.fim.pakt.model.Customer;
import edu.dhbw.mos.fim.pakt.model.IndividualProduct;
import edu.dhbw.mos.fim.pakt.model.Order;
import edu.dhbw.mos.fim.pakt.model.Product;
import edu.dhbw.mos.fim.pakt.model.Reading;
import edu.dhbw.mos.fim.test.DbHelper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
public class DbTest
{
	@Inject
	private ProductRepository productRepository;

	@Inject
	private IndividualProductRepository individualProductRepository;

	@Inject
	private CustomerRepository customerRepository;

	@Inject
	private OrderRepository orderRepository;

	@Inject
	private DbHelper dbHelper;

	private static final String TEST_PRODUCT_NAME = "My fancy product";

	private static final String TEST_READING1_NAME = "myReading1";
	private static final String TEST_READING1_VALUE = "ABCäöü, Я люблю программировать";

	private static final String TEST_READING2_NAME = "something quite Bizarre";
	private static final int TEST_READING2_VALUE = 1234567;

	private static final String TEST_READING3_NAME = "contactPressure";
	private static final double TEST_READING3_VALUE = 6.7823;
	private static final String TEST_READING3_UNIT = "N";

	private final Faker faker = new Faker();

	@AfterEach
	@BeforeEach
	@Transactional
	public void clearDb()
	{
		dbHelper.clearDb();
	}

	@Test
	@Transactional
	public void testCreateProduct()
	{
		productRepository.persist(new Product(TEST_PRODUCT_NAME));

		final var prod = productRepository.findByName(TEST_PRODUCT_NAME);
		assertNotNull(prod);
		assertEquals(TEST_PRODUCT_NAME, prod.getName());
	}

	@Test
	@Transactional
	public void testCreateCustomer()
	{
		final String firstName = faker.name().firstName();
		final String lastName = faker.name().lastName();
		final String email = faker.internet().emailAddress(firstName + "." + lastName);
		final var customer = new Customer(firstName, lastName, email);
		customerRepository.persist(customer);

		final var customerFromDb = customerRepository.findById(customer.getId());
		assertNotNull(customerFromDb);
		assertEquals(firstName, customerFromDb.getGivenName());
		assertEquals(lastName, customerFromDb.getFamilyName());
		assertEquals(email, customerFromDb.getEmail());
	}

	@Test
	@Transactional
	public void testCreateIndividualProduct()
	{
		final var product = new Product(TEST_PRODUCT_NAME);
		productRepository.persist(product);

		final String firstName = faker.name().firstName();
		final String lastName = faker.name().lastName();
		final String email = faker.internet().emailAddress(firstName + "." + lastName);
		final var customer = new Customer(firstName, lastName, email);
		customerRepository.persist(customer);

		final var order = new Order(customer);
		orderRepository.persist(order);

		final String productId = UUID.randomUUID().toString();

		final var iprod1 = new IndividualProduct(productId, product);
		iprod1.setOrder(order);
		final var readings = iprod1.getReadings();

		readings.put(new Reading(TEST_READING1_NAME, TEST_READING1_VALUE));
		readings.put(new Reading(TEST_READING2_NAME, TEST_READING2_VALUE));
		readings.put(new Reading(TEST_READING3_NAME, TEST_READING3_VALUE, TEST_READING3_UNIT));

		individualProductRepository.persist(iprod1);

		final var readIprod1 = individualProductRepository.findBySerialNumber(productId);
		System.out.println(readIprod1);
		assertNotNull(readIprod1);
		assertEquals(productId, readIprod1.getSerialNumber());
		assertEquals(product, readIprod1.getProduct());

		assertEquals(TEST_READING1_VALUE, readIprod1.getReadings().get(TEST_READING1_NAME).getValue());
		assertEquals(TEST_READING2_VALUE, readIprod1.getReadings().get(TEST_READING2_NAME).getValueAsNumber().intValue());

		final var reading3 = readIprod1.getReadings().get(TEST_READING3_NAME);
		assertEquals(TEST_READING3_VALUE, reading3.getValueAsNumber().doubleValue());
		assertEquals(TEST_READING3_UNIT, reading3.getUnit());
	}
}
