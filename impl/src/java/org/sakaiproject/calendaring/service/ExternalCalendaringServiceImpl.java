package org.sakaiproject.calendaring.service;

import lombok.extern.apachecommons.CommonsLog;

import org.sakaiproject.calendaring.model.ExternalCalendarEvent;

/**
 * Implementation of {@link ExternalCalendaringService}
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
@CommonsLog
public class ExternalCalendaringServiceImpl implements ExternalCalendaringService {

	/**
	 * {@inheritDoc}
	 */
	public ExternalCalendarEvent createEvent() {
		return new ExternalCalendarEvent();
	}
	
	
	/**
	 * init
	 */
	public void init() {
		log.info("init");
	}
	
}
