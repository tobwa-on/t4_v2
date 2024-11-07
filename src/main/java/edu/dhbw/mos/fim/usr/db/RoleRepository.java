package edu.dhbw.mos.fim.usr.db;

import edu.dhbw.mos.fim.usr.model.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role>
{
	public Role findByName(final String name)
	{
		return find("name", name).firstResult();
	}

	@Transactional
	public Role findByNameOrCreateNew(final String name)
	{
		Role role = findByName(name);

		if (role == null)
		{
			role = new Role(name);
			persist(role);
		}

		return role;
	}
}
