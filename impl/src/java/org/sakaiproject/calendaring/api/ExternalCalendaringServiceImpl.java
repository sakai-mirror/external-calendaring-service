package org.sakaiproject.calendaring.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

import org.sakaiproject.calendar.api.CalendarEvent;
import org.sakaiproject.calendaring.logic.SakaiProxy;
import org.sakaiproject.time.api.TimeRange;
import org.sakaiproject.user.api.User;



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
	public VEvent createEvent(CalendarEvent event) {
		return createEvent(event, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public VEvent createEvent(CalendarEvent event, List<User> attendees) {
		
		//timezone. All dates are in GMT so we need to explicitly set that
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone("GMT");
		VTimeZone tz = timezone.getVTimeZone();

		//start and end date
		DateTime start = new DateTime(getStartDate(event.getRange()).getTime());
		DateTime end = new DateTime(getEndDate(event.getRange()).getTime());
		
		//create event incl title/summary
		VEvent vevent = new VEvent(start, end, event.getDisplayName());
			
		//add timezone
		vevent.getProperties().add(tz.getTimeZoneId());
		
		//add uid to event
		vevent.getProperties().add(new Uid(event.getId()));
			
		//add description to event
		vevent.getProperties().add(new Description(event.getDescription()));
		
		//add location to event
		vevent.getProperties().add(new Location(event.getLocation()));
		
		//add organiser to event
		Attendee creator = new Attendee(URI.create("mailto:" + sakaiProxy.getUserEmail(event.getCreator())));
		creator.getParameters().add(Role.CHAIR);
		creator.getParameters().add(new Cn(sakaiProxy.getUserDisplayName(event.getCreator())));
		vevent.getProperties().add(creator);
		
		//add attendees to event with 'required participant' role
		if(attendees != null){
			for(User u: attendees) {
				Attendee a = new Attendee(URI.create("mailto:" + u.getEmail()));
				a.getParameters().add(Role.REQ_PARTICIPANT);
				a.getParameters().add(new Cn(u.getDisplayName()));
			
				vevent.getProperties().add(a);
			}
		}
		
		//log.debug("VEvent:" + vevent);
		System.out.println("VEvent:" + vevent);
		
		return vevent;
	}

	
	
	/**
	 * {@inheritDoc}
	 */
	public Calendar createCalendar(List<VEvent> events) {
		
		//setup calendar
		Calendar calendar = setupCalendar();
			
		//add vevents to calendar
		calendar.getComponents().addAll(events);
		
		//validate
		try {
			calendar.validate(true);
		} catch (ValidationException e) {
			e.printStackTrace();
			return null;
		}
		
		//log.debug("Calendar:" + calendar);
		System.out.println("Calendar:" + calendar);
		
		return calendar;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toFile(Calendar calendar) {
		
		String path = generateFilePath(String.valueOf(new Date().getTime()));
		
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(path);
		
			CalendarOutputter outputter = new CalendarOutputter();
			outputter.output(calendar, fout);
		
			fout.flush();
			fout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return path;
		
	}
	
	
	/**
	 * Helper method to setup the standard parts of the calendar
	 * @return
	 */
	private Calendar setupCalendar() {
		
		String serverName = sakaiProxy.getServerName();
		
		//setup calendar
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//"+serverName+"//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		return calendar;
	}
	
	
	
	
	/**
	 * Helper to extract the startDate of a TimeRange into a java.util.Calendar object. 
	 * @param range 
	 * @return
	 */
	private java.util.Calendar getStartDate(TimeRange range) {
		java.util.Calendar c = new GregorianCalendar();
		c.setTimeInMillis(range.firstTime().getTime());
		return c;
	}
	
	/**
	 * Helper to extract the endDate of a TimeRange into a java.util.Calendar object. 
	 * @param range 
	 * @return
	 */
	private java.util.Calendar getEndDate(TimeRange range) {
		java.util.Calendar c = new GregorianCalendar();
		c.setTimeInMillis(range.lastTime().getTime());
		return c;
	}
	
	/**
	 * Helper to create the name of the ICS file we are to write
	 * @param filename
	 * @return
	 */
	private String generateFilePath(String filename) {
		StringBuilder sb = new StringBuilder();
		sb.append(sakaiProxy.getCalendarFilePath());
		sb.append(File.separator);
		sb.append(filename);
		sb.append(".ics");
		return sb.toString();
	}
	
	/**
	 * init
	 */
	public void init() {
		log.info("init");
	}
	
	@Setter
	private SakaiProxy sakaiProxy;
	
}
