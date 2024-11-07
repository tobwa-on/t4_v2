/**
 * Created on 23.02.2024 by auch
 */
package edu.dhbw.mos.fim.pakt.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

/**
 *
 */
@QuarkusTest
// TestHTTPEndpoint currently does not work together with @ApplicationPath
// @TestHTTPEndpoint(CustomerResource.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ProductResourceRestTest
{
	private static final String ENDPOINT = "/api/products";

	@Test
	// see
	// https://quarkus.io/guides/security-jwt#integration-testing-security-annotation
	@TestSecurity(user = "user", roles = { "admin", "user" })
	public void test()
	{
		when().get(ENDPOINT + "/all").then().statusCode(200).body(is("[]"));

		final String productName = "My new product " + UUID.randomUUID();
		final var productMap = Map.of("name", productName);

		final var response = given().contentType(ContentType.JSON).body(productMap).when().post(ENDPOINT).prettyPeek()
				.then().contentType(ContentType.JSON).extract();

		assertEquals(productName, response.path("name"));

		final var id = response.path("id");

		final var response2 = given().pathParam("id", id).when().get(ENDPOINT + "/{id}").prettyPeek().then()
				.contentType(ContentType.JSON).extract();
		assertEquals(productName, response2.path("name"));

		// update product name
		final String productName2 = "My changed product " + UUID.randomUUID();
		final var productMap2 = Map.of("name", productName2);
		given().pathParam("id", id).contentType(ContentType.JSON).body(productMap2).when().put(ENDPOINT + "/{id}")
				.prettyPeek().then().statusCode(204);

		final var response3 = given().pathParam("id", id).when().get(ENDPOINT + "/{id}").prettyPeek().then()
				.contentType(ContentType.JSON).extract();
		assertEquals(productName2, response3.path("name"));

		// delete
		given().pathParam("id", id).when().delete(ENDPOINT + "/{id}").then().statusCode(204);
		given().pathParam("id", id).when().get(ENDPOINT + "/{id}").then().statusCode(404);
	}
}
