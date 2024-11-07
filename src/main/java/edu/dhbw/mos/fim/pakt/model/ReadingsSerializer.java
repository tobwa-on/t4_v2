/**
 *
 */
package edu.dhbw.mos.fim.pakt.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 *
 */
public class ReadingsSerializer extends StdSerializer<Readings>
{
	private static final long serialVersionUID = 5572215207793237134L;

	public ReadingsSerializer()
	{
		this(null);
	}

	public ReadingsSerializer(final Class<Readings> t)
	{
		super(t);
	}

	@Override
	public void serialize(final Readings value, final JsonGenerator gen, final SerializerProvider provider)
			throws IOException
	{
		gen.writeStartArray();
		for (final var r : value.getReadings().values())
			gen.writeObject(r);
		gen.writeEndArray();
	}
}
