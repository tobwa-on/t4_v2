package edu.dhbw.mos.fim.pakt.startup;

import java.util.Set;

import edu.dhbw.mos.fim.usr.db.UserRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

/**
 * This class provides Demo user entries.
 */
@Singleton
public class CreateUsers
{
	@Inject
	private UserRepository userRepo;

	@Transactional
	public void onStartup(@Observes final StartupEvent event)
	{
		if (userRepo.count() == 0)
		{
			userRepo.createAndPersist("admin", "root", Set.of("user", "admin"));
			userRepo.createAndPersist("admin2", "root2", Set.of("user", "admin"));
			userRepo.createAndPersist("user", "user", Set.of("user"));
			userRepo.createAndPersist("user2", "user2", Set.of("user"));
		}
	}
}
