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
			userRepo.createAndPersist("admin", "on23", Set.of("user", "admin"));
		}
	}
}
