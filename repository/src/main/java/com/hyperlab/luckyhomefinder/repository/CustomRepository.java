package com.hyperlab.luckyhomefinder.repository;

import org.springframework.stereotype.Service;

import com.hyperlab.luckyhomefinder.common.domain.Property;
@Service
public interface CustomRepository {
/**
 * Get last property was added to the database.
 * @return last property added 
 * */
	public Property findLastProperty();
}
