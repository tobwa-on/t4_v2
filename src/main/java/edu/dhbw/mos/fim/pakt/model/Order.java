/**
 * by auch
 */
package edu.dhbw.mos.fim.pakt.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order
{
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(optional = false)
	private Customer customer;

	@Column(length = 64, nullable = true, unique = true)
	private String externalId;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "order")
	private List<IndividualProduct> products;

	public Order()
	{
	}

	public Order(final Customer customer)
	{
		this(customer, null);
	}

	public Order(final Customer customer, final String externalId)
	{
		this.customer = customer;
		this.externalId = externalId;
		this.products = new ArrayList<>();
	}

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public Customer getCustomer()
	{
		return customer;
	}

	public void setCustomer(final Customer customer)
	{
		this.customer = customer;
	}

	public List<IndividualProduct> getProducts()
	{
		return products;
	}

	public void setProducts(final List<IndividualProduct> products)
	{
		this.products = products;
	}

	public String getExternalId()
	{
		return externalId;
	}

	public void setExternalId(final String externalId)
	{
		this.externalId = externalId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Order))
			return false;
		final Order other = (Order) obj;
		if (customer == null)
		{
			if (other.customer != null)
				return false;
		}
		else if (!customer.equals(other.customer))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return String.format("Order [id=%s, customer=%s, externalId=%s]", id, customer, externalId);
	}
}
