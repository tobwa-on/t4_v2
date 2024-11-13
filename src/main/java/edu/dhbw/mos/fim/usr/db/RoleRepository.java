package edu.dhbw.mos.fim.usr.db;

import edu.dhbw.mos.fim.usr.model.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role>
{
	@Transactional
	public Role findByNameOrCreateNew(String roleName) {
		Role role = find("name", roleName).firstResult();
		if (role == null) {
			role = new Role(roleName);
			persist(role);  // Neue Rolle speichern
		}
		return role;
	}
}
