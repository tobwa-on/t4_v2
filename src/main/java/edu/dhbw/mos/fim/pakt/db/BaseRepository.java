/**
 * by auch
 */
package edu.dhbw.mos.fim.pakt.db;

import java.util.stream.Stream;

import edu.dhbw.mos.fim.helper.ModelUtils;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import jakarta.transaction.Transactional;

public abstract class BaseRepository<E> implements PanacheRepository<E>
{
	private final BroadcastProcessor<E> persistEventProcessor = BroadcastProcessor.create();

	public BroadcastProcessor<E> persistEventProcessor()
	{
		return persistEventProcessor;
	}

	@Override
	public void persist(final E entity)
	{
		PanacheRepository.super.persist(entity);
		persistEventProcessor().onNext(entity);
	}

	@Override
	public void persistAndFlush(final E entity)
	{
		PanacheRepository.super.persistAndFlush(entity);
		persistEventProcessor().onNext(entity);
	}

	@Override
	public void persist(final Iterable<E> entities)
	{
		PanacheRepository.super.persist(entities);
		for (final var entity : entities)
			persistEventProcessor().onNext(entity);
	}

	@Override
	public void persist(final Stream<E> entities)
	{
		entities.forEach(this::persist);
	}

	@Override
	public void persist(final E firstEntity, @SuppressWarnings("unchecked") final E... entities)
	{
		persist(firstEntity);
		for (final var entity : entities)
			persist(entity);
	}

	@Transactional
	public E update(final Long id, final E updated)
	{
		final E entity = findById(id);

		if (entity != null)
		{
			ModelUtils.copyProperties(updated, entity);
			flush();
		}

		return entity;
	}
}
