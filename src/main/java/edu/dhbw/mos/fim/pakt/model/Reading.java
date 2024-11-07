package edu.dhbw.mos.fim.pakt.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.json.bind.annotation.JsonbTransient;

public class Reading implements Serializable
{
	private static final long serialVersionUID = -634037719556614094L;

	private String name;
	private Object value;
	private String unit;

	public Reading()
	{
	}

	public Reading(final String name, final String value, final String unit)
	{
		this.name = name;
		this.value = value;
		this.unit = unit;
	}

	public Reading(final String name, final Number value, final String unit)
	{
		this.name = name;
		this.value = value;
		this.unit = unit;
	}

	public Reading(final String name, final String value)
	{
		this(name, value, null);
	}

	public Reading(final String name, final Number value)
	{
		this(name, value, null);
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public Object getValue()
	{
		return value;
	}

	@JsonbTransient
	@JsonIgnore
	public Number getValueAsNumber()
	{
		return (Number) value;
	}

	public void setValue(final Object value)
	{
		this.value = value;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(final String unit)
	{
		this.unit = unit;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Reading))
			return false;
		final Reading other = (Reading) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return String.format("[name=%s, value=%s, unit=%s]", name, value, unit);
	}
}
