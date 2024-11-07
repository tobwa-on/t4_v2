package edu.dhbw.mos.fim.pakt.db;

import java.sql.SQLException;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;

/**
 * @author auch
 *
 */
@ApplicationScoped
public class DatabaseLoader
{
	private static final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

	@ConfigProperty(name = "quarkus.datasource.db-kind")
	private String dbKind;

	private Server databaseServer;

	public void onContextInitialized(@Observes @Initialized(ApplicationScoped.class) final Object event)
	{
		logger.info("DatabaseLoader using DB {}", dbKind);

		// Only start embedded h2 DB if needed
		if (!"h2".equalsIgnoreCase(dbKind))
			return;

		try
		{
			synchronized(this)
			{
				if(databaseServer == null)
				{
					databaseServer = Server.createTcpServer("-tcpPort", "2000", "-ifNotExists").start();
					logger.info("Started H2 db on port " + databaseServer.getPort());
				}
			}
		}
		catch (final SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void onStart(@Observes final StartupEvent event)
	{
		logger.info("Application Startup Event");
	}

	public void onStop(@Observes final ShutdownEvent event)
	{
		logger.info("Application Shutdown Event");

		synchronized(this)
		{
			if(databaseServer != null)
			{
				databaseServer.stop();
				databaseServer = null;
				logger.info("Stopped H2 database server");
			}
		}
	}
}
