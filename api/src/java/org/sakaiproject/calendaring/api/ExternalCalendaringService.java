package org.sakaiproject.calendaring.api;

import java.io.File;

import org.sakaiproject.calendar.api.CalendarEvent;

/**
 * A service to provide integrations with external calendars using the iCalendar standard.
 * <p>
 * Tools and services can leverage this to generate an ICS file for an event, as well as make updates
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
public interface ExternalCalendaringService {

	/**
	 * Creates an ICS file for a CalendarEvent.
	 * 
	 * @return File, the ICS file for the given event or null if there was an error
	 */
	public File createEvent(CalendarEvent event);
	
	
}
