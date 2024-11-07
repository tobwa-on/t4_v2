/**
 *
 */
package edu.dhbw.mos.fim.pakt.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 *
 */
public class ReadingsDeserializer extends StdDeserializer<Readings>
{
	public ReadingsDeserializer()
	{
		this(null);
	}

	public ReadingsDeserializer(final Class<?> vc)
	{
		super(vc);
	}

	private static final long serialVersionUID = -92401370659575063L;

	@Override
	public Readings deserialize(final JsonParser p, final DeserializationContext ctxt)
			throws IOException, JacksonException
	{
		final var readings = new Readings();
		if (p.isExpectedStartArrayToken())
		{
			p.nextToken();
			final var iter = p.readValuesAs(Reading.class);
			while (iter.hasNext())
			{
				readings.put(iter.next());
			}
		}
		return readings;
	}
}
