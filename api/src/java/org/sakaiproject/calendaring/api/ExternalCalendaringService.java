package org.sakaiproject.calendaring.api;

import java.util.List;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;

import org.sakaiproject.calendar.api.CalendarEvent;
import org.sakaiproject.user.api.User;

/**
 * A service to provide integrations with external calendars using the iCalendar standard.
 * <p>
 * Tools and services can leverage this to generate an ICS file for an event, as well as make updates.
 * 
 * Ref: http://www.ietf.org/rfc/rfc2445.txt
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
public interface ExternalCalendaringService {

	/**
	 * Creates an iCal VEvent for a Sakai CalendarEvent.
	 * This must then be turned into a Calendar before it can be turned into an ICS file.
	 * 
	 * If the CalendarEvent has the field 'vevent_uuid', that will be used as the UUID of the VEvent preferentially.
	 * If the CalendarEvent has the field 'vevent_sequence', that will be used as the sequence of the VEvent preferentially.
	 * 
	 * @param event Sakai CalendarEvent
	 * @return the VEvent for the given event or null if there was an error
	 */
	public VEvent createEvent(CalendarEvent event);
	
	/**
	 * Creates an iCal VEvent for a Sakai CalendarEvent with the given attendees.
	 * This must then be turned into a Calendar before it can be turned into an ICS file.
	 * 
	 * If the CalendarEvent has the field 'vevent_uuid', that will be used as the UUID of the VEvent preferentially.
	 * If the CalendarEvent has the field 'vevent_sequence', that will be used as the sequence of the VEvent preferentially.
	 * 
	 * @param event Sakai CalendarEvent
	 * @param attendees list of Users that have been invited to the event
	 * @return the VEvent for the given event or null if there was an error
	 */
	public VEvent createEvent(CalendarEvent event, List<User> attendees);
	
	/**
	 * Adds a list of attendees to an existing VEvent.
	 * This must then be turned into a Calendar before it can be turned into an ICS file. 
	 * 
	 * @param vevent  The VEvent to add the attendess too
	 * @param attendees list of Users that have been invited to the event
	 * @return
	 */
	public VEvent addAttendeesToEvent(VEvent vevent, List<User> attendees);
	
	/**
	 * Set the status of an existing VEvent to cancelled.
	 * This must then be turned into a Calendar before it can be turned into an ICS file.
	 * 
	 * @param vevent The VEvent to cancel
	 * @return the updated VEvent
	 */
	public VEvent cancelEvent(VEvent vevent);
	
	/**
	 * Creates an iCal calendar from a list of VEvents.
	 * 
	 * @param events iCal VEvents
	 * @return the Calendar for the given events or null if there was an error
	 */
	public Calendar createCalendar(List<VEvent> events);
	
	/**
	 * Write an iCal calendar out to a file in the filesystem and return the path.
	 * @param calendar iCal calendar object
	 * @return the path to the file
	 */
	public String toFile(Calendar calendar);
	
	/**
	 * Is the ICS service enabled? Tools can use this public method for test in their own UIs.
	 * If this is disabled, nothing will be generated.
	 * @return
	 */
	public boolean isIcsEnabled();
	
	
}
