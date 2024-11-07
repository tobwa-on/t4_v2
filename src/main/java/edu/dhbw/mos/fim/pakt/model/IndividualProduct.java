package edu.dhbw.mos.fim.pakt.model;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.dhbw.mos.fim.pakt.db.ReadingsType;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @see https://schema.org/IndividualProduct
 *
 * @author auch
 *
 */
@Entity
@Table(name = "individualproducts")
public class IndividualProduct
{
	@Id
	@GeneratedValue
	private long id;

	@Column(length = 64, nullable = false, unique = true)
	private String serialNumber;

	@Column(length = 64, nullable = true, unique = true)
	private String externalId;

	// @ManyToOne(fetch = FetchType.EAGER, optional = true)
	// private OrderItem orderItem;

	@JsonIgnore
	@ManyToOne(optional = true)
	private Order order;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Product product;

	@Type(ReadingsType.class)
	@Column(columnDefinition = "jsonb")
	@JsonbTransient // gave up on this one for the current implementation of
									// graphql-java
	private Readings readings;

	public IndividualProduct()
	{
	}

	public IndividualProduct(final String serialNumber, final Product product)
	{
		this.serialNumber = serialNumber;
		this.product = product;
	}

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public String getSerialNumber()
	{
		return serialNumber;
	}

	public void setSerialNumber(final String serialNumber)
	{
		this.serialNumber = serialNumber;
	}

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(final Product product)
	{
		this.product = product;
	}

	@JsonbTransient
	public Readings getReadings()
	{
		if (readings == null)
			readings = new Readings();

		return readings;
	}

	public void setReadings(final Readings readings)
	{
		this.readings = readings;
	}

	public String getExternalId()
	{
		return externalId;
	}

	public void setExternalId(final String externalId)
	{
		this.externalId = externalId;
	}

	public Order getOrder()
	{
		return order;
	}

	public void setOrder(final Order order)
	{
		this.order = order;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((serialNumber == null) ? 0 : serialNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof IndividualProduct))
			return false;
		final IndividualProduct other = (IndividualProduct) obj;
		if (id != other.id)
			return false;
		if (serialNumber == null)
		{
			if (other.serialNumber != null)
				return false;
		}
		else if (!serialNumber.equals(other.serialNumber))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return String.format("IndividualProduct [serialNumber=%s, product=%s, order=%s, readings=%s]", serialNumber,
				product, order, readings);
	}
}
