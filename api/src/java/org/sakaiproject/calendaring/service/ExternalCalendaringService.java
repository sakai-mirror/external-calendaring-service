package org.sakaiproject.calendaring.service;

import org.sakaiproject.calendaring.model.ExternalCalendarEvent;

/**
 * A service to provide integrations with external calendars using the iCalendar standard.
 * <p>
 * Tools and services can leverage this to generate an ICS file for an event, as well as send updates.
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
public interface ExternalCalendaringService {

	/**
	 * Create an event
	 * @return
	 */
	public ExternalCalendarEvent createEvent();
	
	
}
