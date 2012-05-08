package org.sakaiproject.calendaring;

import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Version;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sakaiproject.calendar.api.CalendarEvent;
import org.sakaiproject.calendar.api.CalendarEventEdit;
import org.sakaiproject.calendaring.api.ExternalCalendaringServiceImpl;
import org.sakaiproject.calendaring.mocks.MockCalendarEventEdit;
import org.sakaiproject.calendaring.mocks.MockTimeService;
import org.sakaiproject.time.api.Time;
import org.sakaiproject.time.api.TimeRange;
import org.sakaiproject.time.api.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test class for the ExternalCalendaringService
 * @author Steve Swinsburg (Steve.swinsburg@gmail.com)
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-components.xml"})
public class ExternalCalendaringServiceTest {

	private final String EVENT_NAME = "A new event";
	private final String LOCATION = "Building 1";
	private final String DESCRIPTION = "This is a sample event.";
	private final String UID = "bcb78c02-a738-4224-8e58-c5efed519e00";
	private final long START_TIME = 1336136400; // 4/May/2012 13:00 GMT
	private final long END_TIME = 1336140000; // 4/May/2012 14:00 GMT

	@Autowired
	private ExternalCalendaringServiceImpl service;
	
	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testContext() {
		Assert.assertNotNull(applicationContext.getBean("org.sakaiproject.calendaring.logic.SakaiProxy"));
		Assert.assertNotNull(applicationContext.getBean("org.sakaiproject.calendaring.api.ExternalCalendaringService"));
	}
	
	/**
	 * Ensure the event generation works and returns a usable object. Internal test method, but useful.
	 */
	@Test
	public void testGeneratingEvent() {
		
		CalendarEvent event = generateEvent1();
		
		Assert.assertNotNull(event);
		
		//check attributes of the event are set correctly
		Assert.assertEquals(LOCATION, event.getLocation());
		Assert.assertEquals(DESCRIPTION, event.getDescription());
		Assert.assertEquals(EVENT_NAME, event.getDisplayName());
		Assert.assertEquals(UID, event.getId());
		
	}

	/**
	 * Ensure we can get a ical4j Calendar from the generated event.
	 */
	@Test
	public void testGeneratingCalendar() {
		
		CalendarEvent event = generateEvent1();
		
		net.fortuna.ical4j.model.Calendar calendar = service.createEvent(event);
		
		Assert.assertNotNull(calendar);
		
		//check attributes of the ical4j calendar are what we expect and match those in the event
		Assert.assertEquals(Version.VERSION_2_0, calendar.getVersion());
		Assert.assertEquals(CalScale.GREGORIAN, calendar.getCalendarScale());
	
		//TODO more checks
	}
	
	
	/**
	 * Helper to generate an event. NOT A TEST METHOD
	 * @return
	 */
	private CalendarEvent generateEvent1() {
		
		MockCalendarEventEdit edit = new MockCalendarEventEdit();
		
		edit.setDisplayName(EVENT_NAME);
		edit.setLocation(LOCATION);
		edit.setDescription(DESCRIPTION);
		edit.setId(UID);

		TimeService timeService = new MockTimeService();
		Time start = timeService.newTime(START_TIME);
		Time end = timeService.newTime(END_TIME);
		TimeRange timeRange = timeService.newTimeRange(start, end, true, false);
		
		edit.setRange(timeRange);
		
		//TODO set recurrencerule, then add to test above

		
		return edit;
	}
	
	
	

}
