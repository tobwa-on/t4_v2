/**
 * by auch
 */
package edu.dhbw.mos.fim.pakt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

/**
 * @see https://schema.org/Person
 *
 * @author auch
 *
 */
@Entity
@Table(name = "customers")
public class Customer
{
	@Id
	@GeneratedValue
	private Long id;

	@Column(length = 64, nullable = false)
	private String givenName;

	@Column(length = 64, nullable = false)
	private String familyName;

	@Email
	@Column(length = 128, nullable = true)
	private String email;

	public Customer()
	{
		super();
	}

	public Customer(final String givenName, final String familyName, @Email final String email)
	{
		super();
		this.givenName = givenName;
		this.familyName = familyName;
		this.email = email;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public String getGivenName()
	{
		return givenName;
	}

	public void setGivenName(final String givenName)
	{
		this.givenName = givenName;
	}

	public String getFamilyName()
	{
		return familyName;
	}

	public void setFamilyName(final String familyName)
	{
		this.familyName = familyName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	@Override
	public String toString()
	{
		return String.format("Customer [name=%s %s, email=%s]", givenName, familyName, email);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((familyName == null) ? 0 : familyName.hashCode());
		result = prime * result + ((givenName == null) ? 0 : givenName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Customer))
			return false;
		final Customer other = (Customer) obj;
		if (email == null)
		{
			if (other.email != null)
				return false;
		}
		else if (!email.equals(other.email))
			return false;
		if (familyName == null)
		{
			if (other.familyName != null)
				return false;
		}
		else if (!familyName.equals(other.familyName))
			return false;
		if (givenName == null)
		{
			if (other.givenName != null)
				return false;
		}
		else if (!givenName.equals(other.givenName))
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
}
