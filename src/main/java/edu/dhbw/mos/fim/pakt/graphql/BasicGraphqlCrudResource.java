/**
 * Created on 23.02.2024 by auch
 */
package edu.dhbw.mos.fim.pakt.graphql;

import java.util.List;

import org.eclipse.microprofile.graphql.Name;

import edu.dhbw.mos.fim.pakt.db.BaseRepository;
import edu.dhbw.mos.fim.pakt.db.RepositoryAware;
import io.smallrye.mutiny.Multi;
import jakarta.transaction.Transactional;

/**
 *
 */
class BasicGraphqlCrudResource<E, BR extends BaseRepository<E>> extends RepositoryAware<E, BR>
{
	public List<E> getAll()
	{
		return getRepository().listAll();
	}

	public E get(final Long id)
	{
		final var entity = getRepository().findById(id);
		return entity;
	}

	@Transactional
	public E add(final E entity)
	{
		getRepository().persist(entity);
		return entity;
	}

	@Transactional
	public E update(final Long id, final E updated)
	{
		return getRepository().update(id, updated);
	}

	@Transactional
	public boolean delete(@Name("id") final Long id)
	{
		return getRepository().deleteById(id);
	}

	public Multi<E> createdOrUpdated()
	{
		return getRepository().persistEventProcessor();
	}
}
