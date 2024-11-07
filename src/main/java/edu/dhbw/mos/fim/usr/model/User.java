/**
 *
 */
package edu.dhbw.mos.fim.usr.model;

import java.util.HashSet;
import java.util.Set;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "users")
@UserDefinition
public class User
{
	@Id
	@GeneratedValue
	private long id;

	@Username
	@Column(length = 64, nullable = false, unique = true)
	private String uid;

	@Password
	@Column(length = 256)
	private String password;

	@Roles
	@ManyToMany
	private Set<Role> roles = new HashSet<>();

	public User(final String uid)
	{
		this.uid = uid;
	}

	public User()
	{
	}

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(final Set<Role> roles)
	{
		this.roles = roles;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;
		final User other = (User) obj;
		if (id != other.id)
			return false;
		if (uid == null)
		{
			if (other.uid != null)
				return false;
		}
		else if (!uid.equals(other.uid))
			return false;
		return true;
	}
}
