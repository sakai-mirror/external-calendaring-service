package org.sakaiproject.calendaring.api;

import java.io.File;
import java.util.List;

import org.sakaiproject.calendar.api.CalendarEvent;
import org.sakaiproject.user.api.User;

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
	
	/**
	 * Creates an ICS file for a CalendarEvent with the given attendees
	 * 
	 * @return File, the ICS file for the given event or null if there was an error
	 */
	public File createEvent(CalendarEvent event, List<User> attendees);
	
	//TODO THIS NEEDS TO RETURN SOMETHING ELSE, NOT A FILE... maybe the calendar object itself?
	//or maybe provide a way to 'finalise' the calendar which writes it out to the file?
	
}
