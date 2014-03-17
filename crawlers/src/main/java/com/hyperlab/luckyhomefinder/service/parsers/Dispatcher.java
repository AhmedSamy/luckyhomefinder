package com.hyperlab.luckyhomefinder.service.parsers;

import java.util.List;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.service.Exceptions.DispatcherException;

/**
 * Dispatcher is used to apply the strategy pattern to be able to change the
 * dispatchers according to the website.
 * 
 * @author Kareem El-Shahawe
 * */
public interface Dispatcher {
	/**
	 * Dispatch {@link PropertyParser} threads to fetch properties from the
	 * target website.
	 * 
	 * @param links
	 *            properties links to be parsed.
	 * 
	 * @return {@link List} of {@link Property} objects fetched from the target
	 *         links.
	 * @throws DispatcherException
	 *             in case of error during dispatching the
	 *             {@link PropertyParser}s.
	 * */
	List<Property> dispatchPropertyFetchers(List<String> links)
			throws DispatcherException;
}
