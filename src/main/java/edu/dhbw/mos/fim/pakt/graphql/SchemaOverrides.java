package edu.dhbw.mos.fim.pakt.graphql;

import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLSchema;
import graphql.schema.visibility.BlockedFields;
import graphql.schema.visibility.GraphqlFieldVisibility;
import jakarta.enterprise.event.Observes;

/**
 * Schema Overrides.
 */
public class SchemaOverrides
{
	public GraphQLSchema.Builder customizeSchema(@Observes final GraphQLSchema.Builder builder)
	{
		// do not export id field in product input.
		// Important: For this to work, the id field has to be nullable, i.e. a Long
		// object.
		final GraphqlFieldVisibility blockedFields = BlockedFields.newBlock().addPattern("ProductInput.id").build();

		// The already configured code registry has some settings that seem
		// important for using subscriptions (NPE: Value in JsonObjects name/value
		// pair cannot be null).
		// To avoid this NPE, we need to "steal" the settings of the current code
		// registry, either by using reflection and breaking privacy, or by invoking
		// build() to recover it from the generated schema. :-(
		final var cr = GraphQLCodeRegistry.newCodeRegistry(builder.build().getCodeRegistry()).fieldVisibility(blockedFields)
				.build();

		return builder.codeRegistry(cr);
	}
}
