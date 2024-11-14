package edu.dhbw.mos.fim.usr.db;

import java.util.Collection;
import java.util.stream.Collectors;

import edu.dhbw.mos.fim.usr.model.Role;
import edu.dhbw.mos.fim.usr.model.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User>
{
	@Inject
	private RoleRepository roleRepo;

	public User findByUid(final String uid)
	{
		return find("uid", uid).firstResult();
	}

	@Transactional
	public void createAndPersist(final String uid, final String password, final Collection<String> roles)
	{
		final var dbRoles = roles.stream().map(rs -> roleRepo.findByNameOrCreateNew(rs)).collect(Collectors.toSet());
		final var user = new User(uid);
		user.setPassword(hashPassword(password));

		for (Role role : dbRoles) {
			user.getRoles().add(role);
			role.getUsers().add(user);
		}
		persist(user);
	}

	/**
	 * Hashes the given cleartext password.
	 *
	 * @param password
	 *          cleartext password
	 * @return hashed value
	 */
	private String hashPassword(final String password)
	{
		return BcryptUtil.bcryptHash(password);
	}
}
