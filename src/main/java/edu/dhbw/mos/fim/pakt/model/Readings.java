package edu.dhbw.mos.fim.pakt.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.annotation.Nullable;

@JsonSerialize(using = ReadingsSerializer.class)
@JsonDeserialize(using = ReadingsDeserializer.class)
public class Readings implements Serializable
{
	private static final long serialVersionUID = -6958203054517158823L;

	private Map<String, Reading> readings;

	public Readings()
	{
		readings = new HashMap<>();
	}

	public Map<String, Reading> getReadings()
	{
		return readings;
	}

	public void setReadings(final Map<String, Reading> readings)
	{
		this.readings = readings;
	}

	public void putAll(final Iterable<Reading> reading)
	{
		for (final Reading r : reading)
			put(r);
	}

	public void put(final Reading reading)
	{
		readings.put(reading.getName(), reading);
	}

	public void put(final String key, final String value)
	{
		put(new Reading(key, value));
	}

	public void put(final String key, final int value)
	{
		put(new Reading(key, value));
	}

	public @Nullable Reading get(final String key)
	{
		return readings.get(key);
	}

	/**
	 * @return unmodifiable collection with all readings.
	 */
	public Collection<Reading> asCollection()
	{
		return Collections.unmodifiableCollection(readings.values());
	}

	@Override
	public String toString()
	{
		return readings.toString();
	}
}
