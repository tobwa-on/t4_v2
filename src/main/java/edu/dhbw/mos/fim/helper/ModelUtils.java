/**
 * by auch
 */
package edu.dhbw.mos.fim.helper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtilsBean;

public final class ModelUtils
{
	private ModelUtils()
	{
	}

	/**
	 * Copies properties from source to target (non-recursive, shallow). Does
	 * *not* change the id attribute of the target.
	 *
	 * @param source
	 * @param target
	 * @param propertiesToIgnore
	 *          list of property names whose values shall not be copied.
	 */
	public static void copyProperties(final Object source, final Object target, final String... propertiesToIgnore)
	{
		final PropertyUtilsBean pub = new PropertyUtilsBean();

		// see PropertyUtilsBean.copyProperties
		final PropertyDescriptor[] origDescriptors = pub.getPropertyDescriptors(source);
		for (final PropertyDescriptor origDescriptor : origDescriptors)
		{
			final String name = origDescriptor.getName();

			if ("id".equals(name) || isInArray(name, propertiesToIgnore))
				continue;

			if (pub.isReadable(source, name) && pub.isWriteable(target, name))
			{
				try
				{
					final Object value = pub.getSimpleProperty(source, name);
					pub.setSimpleProperty(target, name, value);
				}
				catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
				{
					throw new IllegalArgumentException(String.format("Could not set property %s on %s", name, target.getClass()),
							e);
				}
			}
		}
	}

	private static boolean isInArray(final String name, final String[] propertiesToIgnore)
	{
		// for a small amount of values, a hashset is not significantly faster
		for (final String propName : propertiesToIgnore)
		{
			if (propName.equals(name))
				return true;
		}

		return false;
	}
}
