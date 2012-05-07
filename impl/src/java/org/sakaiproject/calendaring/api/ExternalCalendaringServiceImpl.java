package org.sakaiproject.calendaring.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
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
	public Calendar createEvent(CalendarEvent event) {

		//String serverName = sakaiProxy.getServerName();
		
		//setup
		Calendar calendar = new Calendar();
		//calendar.getProperties().add(new ProdId("-//"+serverName+"//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);

		//add uid
		//calendar.getProperties().add(event.getId());

		//handle all day event?
		
		//handle ranged event

		
		System.out.println(calendar);
		
		return calendar;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Calendar createEvent(CalendarEvent event, List<User> attendees) {
		
		Calendar calendar = createEvent(event);
		
		//add attendees
		
		return calendar;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toFile(Calendar calendar) {
		
		String path = generateFilePath(getUid(calendar));
		
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
	 * @param eventUid
	 * @return
	 */
	private String generateFilePath(String eventUid) {
		StringBuilder sb = new StringBuilder();
		sb.append(sakaiProxy.getCalendarFilePath());
		sb.append(File.separator);
		sb.append(eventUid);
		sb.append(".ics");
		return sb.toString();
	}
	
	/**
	 * Helper to get the UID of a Calendar
	 * @param c	Caledar
	 * @return
	 */
	private String getUid(Calendar c) {
		Property p = c.getProperty(Property.UID);
		return p.getValue();
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
