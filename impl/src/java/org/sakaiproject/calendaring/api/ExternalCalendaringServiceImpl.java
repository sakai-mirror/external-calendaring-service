package org.sakaiproject.calendaring.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.GregorianCalendar;

import javax.annotation.Resource;

import lombok.extern.apachecommons.CommonsLog;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

import org.sakaiproject.calendar.api.CalendarEvent;
import org.sakaiproject.calendaring.logic.SakaiProxy;
import org.sakaiproject.time.api.TimeRange;

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
	public File createEvent(CalendarEvent event) {

		String serverName = sakaiProxy.getServerName();
		
		//setup
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//"+serverName+"//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);

		//add uid
		calendar.getProperties().add(event.getId());

		//handle all day event?
		
		//handle ranged event

		/**
		 
		TimeService timeService = getSakaiFacade().getTimeService();
		Time start = timeService.newTime(startTime.getTime());
		Time end = timeService.newTime(endTime.getTime());
		TimeRange timeRange = timeService.newTimeRange(start, end, true, false);
		eventEdit.setRange(timeRange);

		String desc = meeting.getDescription();
		eventEdit.setDescription(PlainTextFormat.convertFormattedHtmlTextToPlaintext(desc));
		eventEdit.setLocation(meeting.getLocation());
		eventEdit.setDisplayName(meeting.getTitle() + title_suffix);
		eventEdit.setRange(timeRange);
		 
		 */
		
		System.out.println(calendar);
		
		/*
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(generateFilePath(event.getId()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, fout);
		
		fout.flush();
		fout.close();
		*/
		
		return new File("bb");
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
	 * init
	 */
	public void init() {
		log.info("init");
	}
	
	@Resource(name="org.sakaiproject.calendaring.logic.SakaiProxy")
	private SakaiProxy sakaiProxy;
	
}
