/**
 *
 */
package edu.dhbw.mos.fim.pakt.rest;

/**
 *
 */
public class UserCredentials
{
	private String user;
	private String password;

	public UserCredentials(final String user, final String password)
	{
		this.user = user;
		this.password = password;
	}

	public UserCredentials()
	{
		super();
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(final String user)
	{
		this.user = user;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}
}
