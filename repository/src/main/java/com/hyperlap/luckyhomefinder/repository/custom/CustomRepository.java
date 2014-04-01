package com.hyperlap.luckyhomefinder.repository.custom;

import org.springframework.stereotype.Service;

import com.hyperlab.luckyhomefinder.common.domain.Property;

public interface CustomRepository {
	/**
	 * Get last property was added to the database.
	 * 
	 * @param site
	 *            will be used to build the last known property id.
	 * @return last property added to the database.
	 * */
	Property findLastProperty(String site);
}
