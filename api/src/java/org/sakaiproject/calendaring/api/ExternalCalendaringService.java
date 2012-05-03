package org.sakaiproject.calendaring.api;

import java.util.List;

import net.fortuna.ical4j.model.Calendar;

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
	 * Creates an iCal calendar for a Sakai CalendarEvent.
	 * 
	 * @param event Sakai CalendarEvent
	 * @return the Calendar for the given event or null if there was an error
	 */
	public Calendar createEvent(CalendarEvent event);
	
	/**
	 * Creates an iCal calendar for a Sakai CalendarEvenT with the given attendees
	 * 
	 * @param event Sakai CalendarEvent
	 * @param attendees list of Users that have been invited to hte event
	 * @return the Calendar for the given event or null if there was an error
	 */
	public Calendar createEvent(CalendarEvent event, List<User> attendees);
	
	
	/**
	 * Write an iCal calendar out to a file in the filesystem.
	 * @param calendar iCal calendar object
	 * @return the path to the file
	 */
	public String toFile(Calendar calendar);
}
