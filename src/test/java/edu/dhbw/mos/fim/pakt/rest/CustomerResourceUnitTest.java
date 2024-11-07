package edu.dhbw.mos.fim.pakt.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.github.javafaker.Faker;

import edu.dhbw.mos.fim.pakt.model.Customer;
import edu.dhbw.mos.fim.test.DbHelper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;

/**
 * @author auch
 *
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerResourceUnitTest
{
	private static final String CUSTOMER1_GNAME = "Bugs";
	private static final String CUSTOMER1_FNAME = "Bunny";
	private static final String CUSTOMER1_EMAIL = "bugs.bunny@example.org";

	@Inject
	private CustomerResource customerResource;

	@Inject
	private DbHelper dbHelper;

	private final Faker faker = new Faker();

	@AfterAll
	@BeforeAll
	public void clearDb()
	{
		dbHelper.clearDb();
	}

	@Test
	public void test01CheckEmpty()
	{
		assertEquals(0, customerResource.getAll().size());
	}

	@Test
	@TestSecurity(user = "user", roles = { "admin", "user" })
	public void test02AddCustomer()
	{
		final var customer = new Customer(CUSTOMER1_GNAME, CUSTOMER1_FNAME, CUSTOMER1_EMAIL);
		final long id = customerResource.add(customer).getId();

		assertEquals(1, customerResource.getAll().size());
		assertTrue(customerResource.getAll().contains(customerResource.get(id)));
		final var dbCustomer = customerResource.get(id);
		assertEquals(customer.getGivenName() + customer.getFamilyName() + customer.getEmail(),
				dbCustomer.getGivenName() + dbCustomer.getFamilyName() + dbCustomer.getEmail());
	}

	@Test
	@TestSecurity(user = "user", roles = { "admin", "user" })
	public void test03ChangeCustomer()
	{
		final var customer = new Customer(faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress());

		final long id = customerResource.getAll().get(0).getId();

		System.out.println(customer + " " + customer.getId() + " " + id);
		customerResource.update(id, customer);
		final var dbCustomer = customerResource.get(id);
		System.out.println(dbCustomer);

		assertEquals(customer.getGivenName() + customer.getFamilyName() + customer.getEmail(),
				dbCustomer.getGivenName() + dbCustomer.getFamilyName() + dbCustomer.getEmail());

		customerResource.delete(id);
		assertEquals(0, customerResource.getAll().size());
	}
}
