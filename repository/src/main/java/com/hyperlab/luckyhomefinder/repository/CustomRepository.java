package com.hyperlab.luckyhomefinder.repository;

import com.hyperlab.luckyhomefinder.common.domain.Property;

public interface CustomRepository {
/**
 * Get last property was added to the database.
 * @return last property added 
 * */
	public Property findLastProperty();
}
