package edu.dhbw.mos.fim.pakt.graphql;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matcher;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import jakarta.json.Json;

final class Helper
{
	static final String GRAPHQL_PATH = "/graphql";
	static final String GRAPHQL_SCHEMA_PATH = GRAPHQL_PATH + "/schema.graphql";

	static JsonPath submitQuery(final String query)
	{
		final String jsonQuery = toGraphQLQueryBody(query);
		System.out.println("Query: " + jsonQuery);

		return given().when().body(jsonQuery).contentType(ContentType.JSON).accept(ContentType.JSON).post(GRAPHQL_PATH)
				.then().log().body().assertThat().statusCode(200).extract().jsonPath();
	}

	static void submitQueryAndValidate(final String query, final Matcher<?> matcher,
			final Matcher<?>... additionalMatchers)
	{
		final String jsonQuery = toGraphQLQueryBody(query);
		System.out.println("Query: " + jsonQuery);

		given().when().body(jsonQuery).contentType(ContentType.JSON).accept(ContentType.JSON).post(GRAPHQL_PATH).then()
				.log().body().assertThat().statusCode(200).body(matcher, additionalMatchers);
	}

	private static String toGraphQLQueryBody(final String query)
	{
		// creates a query in JSON format
		return Json.createObjectBuilder().add("query", query).addNull("variables").build().toString();
	}
}
