package edu.dhbw.mos.fim.pakt.graphql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ProductResourceTest
{
	@Test
	public void testSchema() throws IOException
	{
		given().when().get(Helper.GRAPHQL_SCHEMA_PATH).then().assertThat().statusCode(200)
				.body(containsString("type Query"));
	}

	private static final String MUT_ADD = """
			mutation {
				addProduct(product: {name:"%s"})
				{id name}
			}""";

	private static final String QUERY_GET = """
			query {
				product(id: %d) {name}
			}""";

	private static final String MUT_UPDATE = """
			mutation {
				updateProduct(id: %d, update:{name:"%s"}) {name}
			}""";

	private static final String MUT_DELETE = """
			mutation {
				deleteProduct(id: %d)
			}""";

	@Test
	public void testCreateAndDelete()
	{
		final String productName = "My new product " + UUID.randomUUID();

		Assertions.assertTrue(Helper.submitQuery("query {allProducts {name}}").getList("data.allProducts").isEmpty());

		final long id = Helper.submitQuery(MUT_ADD.formatted(productName)).getLong("data.addProduct.id");
		Assertions.assertTrue(id != 0);

		Assertions.assertEquals(productName, Helper.submitQuery(QUERY_GET.formatted(id)).getString("data.product.name"));

		final String productNameUpd = "My changed product " + UUID.randomUUID();
		Assertions.assertEquals(productNameUpd,
				Helper.submitQuery(MUT_UPDATE.formatted(id, productNameUpd)).getString("data.updateProduct.name"));

		Assertions.assertTrue(Helper.submitQuery(MUT_DELETE.formatted(id)).getBoolean("data.deleteProduct"));
		Assertions.assertNull(Helper.submitQuery(QUERY_GET.formatted(id)).getString("data.product.name"));
	}
}
