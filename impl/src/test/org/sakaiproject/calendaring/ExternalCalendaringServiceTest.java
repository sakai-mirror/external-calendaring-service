package org.sakaiproject.calendaring;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Version;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sakaiproject.calendar.api.CalendarEvent;
import org.sakaiproject.calendaring.api.ExternalCalendaringServiceImpl;
import org.sakaiproject.calendaring.mocks.MockCalendarEventEdit;
import org.sakaiproject.calendaring.mocks.MockTimeService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.time.api.Time;
import org.sakaiproject.time.api.TimeRange;
import org.sakaiproject.time.api.TimeService;
import org.sakaiproject.user.api.UserAlreadyDefinedException;
import org.sakaiproject.user.api.UserIdInvalidException;
import org.sakaiproject.user.api.UserPermissionException;
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
	private final String CREATOR="steve";
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
		
		CalendarEvent event = generateEvent();
		
		Assert.assertNotNull(event);
		
		//check attributes of the event are set correctly
		Assert.assertEquals(LOCATION, event.getLocation());
		Assert.assertEquals(DESCRIPTION, event.getDescription());
		Assert.assertEquals(EVENT_NAME, event.getDisplayName());
		Assert.assertEquals(UID, event.getId());
		Assert.assertEquals(CREATOR, event.getCreator());
		
	}

	/**
	 * Ensure we can get a ical4j Calendar from the generated event.
	 */
	@Test
	public void testGeneratingCalendar() {
		
		CalendarEvent event = generateEvent();
		
		net.fortuna.ical4j.model.Calendar calendar = service.createEvent(event);
		
		Assert.assertNotNull(calendar);
		
		//check attributes of the ical4j calendar are what we expect and match those in the event
		Assert.assertEquals(Version.VERSION_2_0, calendar.getVersion());
		Assert.assertEquals(CalScale.GREGORIAN, calendar.getCalendarScale());
		
		//TODO get the VEvent from the calendar and check the attributes.
		//Assert.assertEquals(EVENT_NAME, vevent.getProperty("SUMMARY"));
		//Assert.assertEquals(LOCATION, vevent.getProperty("LOCATION"));
	}
	
	@Test
	public void testGeneratingCalendarWithAttendees() {
		
		CalendarEvent event = generateEvent();
		
		net.fortuna.ical4j.model.Calendar calendar = service.createEvent(event, generateUsers());
		
		Assert.assertNotNull(calendar);
	
		//TODO more checks, check attendees etc
		
	}
	
	@Test
	public void testCreatingFile() {
		
		CalendarEvent event = generateEvent();
		
		net.fortuna.ical4j.model.Calendar calendar = service.createEvent(event);
		
		String path = service.toFile(calendar);
		Assert.assertNotNull(path);
		
		//now see if the file actually exists
		File f = new File(path);
		Assert.assertTrue(f.exists());
		
		
	}
	
	/**
	 * Helper to generate an event. NOT A TEST METHOD
	 * @return
	 */
	private CalendarEvent generateEvent() {
		
		MockCalendarEventEdit edit = new MockCalendarEventEdit();
		
		edit.setDisplayName(EVENT_NAME);
		edit.setLocation(LOCATION);
		edit.setDescription(DESCRIPTION);
		edit.setId(UID);
		edit.setCreator(CREATOR);

		TimeService timeService = new MockTimeService();
		Time start = timeService.newTime(START_TIME);
		Time end = timeService.newTime(END_TIME);
		TimeRange timeRange = timeService.newTimeRange(start, end, true, false);
		
		edit.setRange(timeRange);
		
		//TODO set recurrencerule, then add to test above

		
		return edit;
	}
	
	/**
	 * Helper to generate a list of users. NOT A TEST METHOD
	 * @return
	 * @throws UserPermissionException 
	 * @throws UserAlreadyDefinedException 
	 * @throws UserIdInvalidException 
	 */
	private List<User> generateUsers(){
		List<User> users = new ArrayList<User>();
		
		for(int i=0;i<5;i++) {
			
			org.sakaiproject.mock.domain.User u = new org.sakaiproject.mock.domain.User(null, "user"+i, "user"+i, "user"+i, "user"+i+"@email.com", "User", String.valueOf(i),
					null, null, null, null, null,null,null,null,null,null);
			
			users.add(u);
		}
		
		return users;
	}
	
}
