package com.hyperlab.luckyhomefinder.repository.custom;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.hyperlab.luckyhomefinder.common.domain.Property;

/**
 * Responsible for getting the last known property id for the target site.
 * */

public class CustomRepositoryImpl implements CustomRepository {

	/** Mongotemplate providing access to mongodb. */
	@Autowired
	private MongoOperations mongoOperations;


	/**
	 * {@inheritDoc}
	 * */
	public final Property findLastProperty(final String site) {
		final String regx = site;
		Pattern pattern = Pattern.compile(regx);
		Query query = new Query(Criteria.where("link").regex(pattern));
		query.with(new Sort(Sort.Direction.DESC, "siteId"));
		Property result = mongoOperations.findOne(query,
				com.hyperlab.luckyhomefinder.common.domain.Property.class);
		return result;
	}

}
