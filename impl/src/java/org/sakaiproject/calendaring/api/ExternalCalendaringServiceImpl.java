package org.sakaiproject.calendaring.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

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
import net.fortuna.ical4j.model.property.Sequence;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
		
		if(!isIcsEnabled()) {
			log.debug("ExternalCalendaringService is disabled. Enable via calendar.ics.generation.enabled=true in sakai.properties");
			return null;
		}
		
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
		//could come from the vevent_uuid field in the calendar event, otherwise from event ID.
		String uuid = null;
		if(StringUtils.isNotBlank(event.getField("vevent_uuid"))) {
			uuid = event.getField("vevent_uuid");
		} else {
			uuid = event.getId();
		}		
		vevent.getProperties().add(new Uid(uuid));
		
		//add sequence to event
		//will come from the vevent_sequnece field in the calendar event, otherwise skip it
		String sequence = null;
		if(StringUtils.isNotBlank(event.getField("vevent_sequence"))) {
			sequence = event.getField("vevent_sequence");
			vevent.getProperties().add(new Sequence(sequence));
		}
			
		//add description to event
		vevent.getProperties().add(new Description(event.getDescription()));
		
		//add location to event
		vevent.getProperties().add(new Location(event.getLocation()));
		
		//add organiser to event
		if(StringUtils.isNotBlank(event.getCreator())) {
			Attendee creator = new Attendee(URI.create("mailto:" + sakaiProxy.getUserEmail(event.getCreator())));
			creator.getParameters().add(Role.CHAIR);
			creator.getParameters().add(new Cn(sakaiProxy.getUserDisplayName(event.getCreator())));
			vevent.getProperties().add(creator);
		}
		
		//add attendees to event with 'required participant' role
		vevent = addAttendeesToEvent(vevent, attendees);
		
		if(log.isDebugEnabled()){
			log.debug("VEvent:" + vevent);
		}
		
		return vevent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public VEvent addAttendeesToEvent(VEvent vevent, List<User> attendees) {
		
		if(!isIcsEnabled()) {
			log.debug("ExternalCalendaringService is disabled. Enable via calendar.ics.generation.enabled=true in sakai.properties");
			return null;
		}
		
		//add attendees to event with 'required participant' role
		if(attendees != null){
			for(User u: attendees) {
				Attendee a = new Attendee(URI.create("mailto:" + u.getEmail()));
				a.getParameters().add(Role.REQ_PARTICIPANT);
				a.getParameters().add(new Cn(u.getDisplayName()));
			
				vevent.getProperties().add(a);
			}
		}
		
		if(log.isDebugEnabled()){
			log.debug("VEvent with attendees:" + vevent);
		}
		
		return vevent;
	}

	/**
	 * {@inheritDoc}
	 */
	public VEvent cancelEvent(VEvent vevent) {
		
		if(!isIcsEnabled()) {
			log.debug("ExternalCalendaringService is disabled. Enable via calendar.ics.generation.enabled=true in sakai.properties");
			return null;
		}
		
		vevent.getProperties().add(new Status("CANCELLED"));
		
		if(log.isDebugEnabled()){
			log.debug("VEvent cancelled:" + vevent);
		}
		
		return vevent;
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public Calendar createCalendar(List<VEvent> events) {
		
		if(!isIcsEnabled()) {
			log.debug("ExternalCalendaringService is disabled. Enable via calendar.ics.generation.enabled=true in sakai.properties");
			return null;
		}
		
		//setup calendar
		Calendar calendar = setupCalendar();
		
		//null check
		if(CollectionUtils.isEmpty(events)) {
			log.error("List of VEvents was null or empty, no calendar will be created.");
			return null;
		}
		
		//add vevents to calendar
		calendar.getComponents().addAll(events);
		
		//validate
		try {
			calendar.validate(true);
		} catch (ValidationException e) {
			e.printStackTrace();
			return null;
		}
		
		if(log.isDebugEnabled()){
			log.debug("Calendar:" + calendar);
		}
		
		return calendar;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toFile(Calendar calendar) {
		
		if(!isIcsEnabled()) {
			log.debug("ExternalCalendaringService is disabled. Enable via calendar.ics.generation.enabled=true in sakai.properties");
			return null;
		}
		
		//null check
		if(calendar == null) {
			log.error("Calendar is null, cannot generate ICS file.");
			return null;
		}
		
		String path = generateFilePath(UUID.randomUUID().toString());
		
		//test file
		File file = new File(path);
		try {
			if(!file.createNewFile()) {
				log.error("Couldn't write file to: " + path);
				return null;	
			}
		} catch (IOException e) {
			log.error("An error occurred trying to write file to: " + path + " : " + e.getClass() + " : " + e.getMessage());
			return null;
		}
		
		//if cleanup enabled, mark for deletion when the JVM exits.
		if(sakaiProxy.isCleanupEnabled()) {
			file.deleteOnExit();
		}
		
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(file);
		
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
	 * {@inheritDoc}
	 */
	public boolean isIcsEnabled() {
		return sakaiProxy.isIcsEnabled();
	}
	
	/**
	 * Helper method to setup the standard parts of the calendar
	 * @return
	 */
	private Calendar setupCalendar() {
		
		String serverName = sakaiProxy.getServerName();
		
		//setup calendar
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//"+serverName+"//Sakai External Calendaring Service//EN"));
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
		
		String base = sakaiProxy.getCalendarFilePath();
		sb.append(base);
		
		//add slash if reqd
		if(!StringUtils.endsWith(base, File.separator)) {
			sb.append(File.separator);
		}
		
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
