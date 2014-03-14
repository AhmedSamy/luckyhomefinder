package com.hyperlab.luckyhomefinder.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import com.hyperlab.luckyhomefinder.common.domain.Property;

/**
 * PropertyRepository provide Access to {@code Property}s data base records.
 * */
@Service
public interface PropertyRepository extends MongoRepository<Property, UUID> {

	/**
	 * Find the property record using property ID.
	 * 
	 * @param id
	 *            property Id.
	 * @return property record with matching Id.
	 * */
	public Property findById(UUID id);

	/**
	 * Find all properties located in the provided district.
	 * @param district location of property district.
	 * @return List of all properties exist the the same district.
	 * */
	public List<Property> findByDistrict(String district);
	
	
}
