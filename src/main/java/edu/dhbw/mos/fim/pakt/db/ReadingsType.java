package edu.dhbw.mos.fim.pakt.db;

import java.io.Serializable;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Objects;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dhbw.mos.fim.pakt.model.Reading;
import edu.dhbw.mos.fim.pakt.model.Readings;
import jakarta.annotation.Nullable;

public class ReadingsType implements UserType<Readings>
{
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public int getSqlType()
	{
		return SqlTypes.JSON;
	}

	@Override
	public Class<Readings> returnedClass()
	{
		return Readings.class;
	}

	@Override
	public boolean equals(final Readings x, final Readings y)
	{
		return Objects.equals(x, y);
	}

	@Override
	public int hashCode(final Readings x)
	{
		return x.hashCode();
	}

	@Override
	public Readings nullSafeGet(final ResultSet rs, final int position, final SharedSessionContractImplementor session,
			final Object owner) throws SQLException
	{
		final String cellContent = rs.getString(position);
		return readFromString(cellContent);
	}

	@Override
	public void nullSafeSet(final PreparedStatement st, final Readings value, final int index,
			final SharedSessionContractImplementor session) throws SQLException
	{
		if (value == null)
		{
			st.setNull(index, Types.OTHER);
		}
		else
		{
			final var str = writeToString(value);
			st.setObject(index, str, Types.OTHER);
		}
	}

	private Readings readFromString(final String json)
	{
		if (json == null)
			return null;

		try
		{
			final var readings = new Readings();
			final List<Reading> rList = MAPPER.readerForListOf(Reading.class).readValue(json);
			readings.putAll(rList);
			return readings;
		}
		catch (final Exception e)
		{
			throw new IllegalArgumentException("Failed to convert String to Readings: " + e.getMessage(), e);
		}
	}

	private @Nullable String writeToString(@Nullable final Readings value)
	{
		if (value == null)
			return null;

		try
		{
			final StringWriter sw = new StringWriter();
			MAPPER.writerFor(Reading.class).writeValuesAsArray(sw).writeAll(value.asCollection()).close();
			return sw.toString();
		}
		catch (final Exception e)
		{
			throw new IllegalArgumentException("Failed to convert Readings to String: " + e.getMessage(), e);
		}
	}

	@Override
	public Readings deepCopy(final Readings value)
	{
		return readFromString(writeToString(value));
	}

	@Override
	public boolean isMutable()
	{
		return true;
	}

	@Override
	public Serializable disassemble(final Readings value)
	{
		return deepCopy(value);
	}

	@Override
	public Readings assemble(final Serializable cached, final Object owner)
	{
		return deepCopy((Readings) cached);
	}
}
