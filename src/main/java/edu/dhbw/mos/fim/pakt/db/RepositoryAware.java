/**
 * Created on 23.02.2024 by auch
 */
package edu.dhbw.mos.fim.pakt.db;

import com.google.common.reflect.TypeToken;

import jakarta.enterprise.inject.spi.CDI;

/**
 *
 */
public class RepositoryAware<E, BR extends BaseRepository<E>>
{
	private BR repository;

	@SuppressWarnings("unchecked")
	protected BR getRepository()
	{
		if (repository == null)
		{
			@SuppressWarnings("serial")
			final TypeToken<BR> type = new TypeToken<BR>(getClass())
			{
			};

			repository = (BR) CDI.current().select(type.getRawType()).get();
		}

		return repository;
	}
}
