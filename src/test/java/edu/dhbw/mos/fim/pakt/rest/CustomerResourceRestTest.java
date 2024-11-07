/**
 *
 */
package edu.dhbw.mos.fim.pakt.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 * @author auch
 *
 */
@QuarkusTest
// TestHTTPEndpoint currently does not work together with @ApplicationPath
// @TestHTTPEndpoint(CustomerResource.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerResourceRestTest
{
	private static final String ENDPOINT = "/api/customer";

	@Test
	// see
	// https://quarkus.io/guides/security-jwt#integration-testing-security-annotation
	@TestSecurity(user = "user", roles = "admin")
	public void test()
	{
		when().get(ENDPOINT + "/all").then().statusCode(200).body(is("[]"));

		final var customer = Map.of("givenName", "Elmer", "familyName", "Fudd", "email", "elmer@example.org");

		final var response = given().contentType(ContentType.JSON).body(customer).when().post(ENDPOINT).then()
				.contentType(ContentType.JSON).extract();

		assertEquals("Elmer", response.path("givenName"));
		assertEquals("Fudd", response.path("familyName"));
		assertEquals("elmer@example.org", response.path("email"));

		final var id = response.path("id");

		final var response2 = given().pathParam("id", id).when().get(ENDPOINT + "/{id}").then()
				.contentType(ContentType.JSON).extract();
		assertEquals("Elmer", response2.path("givenName"));
		assertEquals("Fudd", response2.path("familyName"));
		assertEquals("elmer@example.org", response2.path("email"));

		given().pathParam("id", id).when().delete(ENDPOINT + "/{id}").then().statusCode(204);
	}
}
