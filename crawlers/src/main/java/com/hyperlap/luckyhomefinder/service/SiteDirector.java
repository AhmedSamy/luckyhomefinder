package com.hyperlap.luckyhomefinder.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.service.Exceptions.LinksFetcherException;
import com.hyperlab.luckyhomefinder.service.parsers.LinksFetcher;
import com.hyperlap.luckyhomefinder.repository.custom.CustomRepository;

/**
 * Director is responsible for Managing the work flow required for each website.
 * */
public class SiteDirector extends Thread {
	/**
	 * Logger.
	 * */
	private static final Logger LOG = Logger.getLogger(SiteDirector.class);

	/**
	 * Responsible for getting last known property ID for designated Site.
	 * */
	@Autowired
	private CustomRepository customRepository;
	/**
	 * Links fetcher responsible of fetching property links.
	 * */
	private LinksFetcher linksFetcher;

	/**
	 * Manager used to manage work flow of the property parser.
	 * */
	private ParsersManager manager;

	/**
	 * Website to manage.
	 * */
	private String website;

	/**
	 * private constructor.
	 * */
	@SuppressWarnings("unused")
	private SiteDirector() {
		// Empty constructor.
	}

	/**
	 * Constructor.
	 * 
	 * @param pLinksFetcher
	 *            Links fetcher used to fetch property links.
	 * @param pManager
	 *            used to manage work flow of property parsers.
	 * @param pWebsite
	 *            target website to manage.
	 * */
	public SiteDirector(final LinksFetcher pLinksFetcher,
			final ParsersManager pManager, final String pWebsite) {
		this.manager = pManager;
		this.linksFetcher = pLinksFetcher;
		this.website = pWebsite;
	}

	@Override
	public void run() {

	}

	/**
	 * Starting point of {@link SiteDirector} work.
	 * */
	@Scheduled(fixedDelay = 10000)
	public final void execute() {
		// Fetching property links for the targeted website.
		try {
			Property lastAddedProperty = customRepository
					.findLastProperty(website);
			List<String> propertyLinks = linksFetcher
					.fetchLinks(lastAddedProperty);
			if (propertyLinks != null && !propertyLinks.isEmpty()) {
				manager.processLinks(propertyLinks);
			} else {
				if (propertyLinks == null) {
					LOG.warn(website + " link fetcher return empty list");
				}
			}
		} catch (LinksFetcherException e) {
			LOG.fatal(e);
		}
	}

}
