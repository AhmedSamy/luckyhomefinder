package com.hyperlab.luckyhomefinder.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.hyperlab.luckyhomefinder.common.domain.Property;

@Repository
public class CustomRepositoryImpl implements CustomRepository {

	/** Mongotemplate providing access to mongodb. */
	@Autowired
	MongoOperations mongoOperations;

	/**
	 * {@inheritDoc}
	 * */
	public final Property findLastProperty() {
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "postDate"));
		Property result = mongoOperations.findOne(query,
				com.hyperlab.luckyhomefinder.common.domain.Property.class);
		return result;
	}

}
