package edu.dhbw.mos.fim.usr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.security.jpa.RolesValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role
{
	@Id
	@GeneratedValue
	private long id;

	@RolesValue
	@Column(length = 64, nullable = false, unique = true)
	private String name;

	@ManyToMany
	@JsonIgnore
	private Set<User> users = new HashSet<>();

	public Role(final String name)
	{
		this.name = name;
	}

	public Role() { }

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public Set<User> getUsers()
	{
		return users;
	}

	public void setUsers(final Set<User> users)
	{
		this.users = users;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Role))
			return false;
		final Role other = (Role) obj;
		if (id != other.id)
			return false;
		if (name == null)
		{
			return other.name == null;
		}
		else return name.equals(other.name);
	}
}
