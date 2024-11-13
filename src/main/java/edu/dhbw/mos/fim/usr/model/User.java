package edu.dhbw.mos.fim.usr.model;

import java.util.HashSet;
import java.util.Set;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@UserDefinition
public class User
{
	@Id
	@Username
	@Column(length = 64, nullable = false, unique = true)
	private String uid;

	@Password
	@Column(length = 256)
	private String password;

	@Roles
	@ManyToMany(mappedBy = "users")
	private Set<Role> roles = new HashSet<>();


	public User(final String uid)
	{
		this.uid = uid;
	}

	public User() {
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	public void setPassword(final String password)
	{
		this.password = password;
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
		if (uid == null)
		{
            return other.uid == null;
		}
		else if (!uid.equals(other.uid))
			return false;
		return true;
	}
}
