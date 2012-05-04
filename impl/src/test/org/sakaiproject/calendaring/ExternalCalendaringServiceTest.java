package org.sakaiproject.calendaring;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.sakaiproject.calendar.api.Calendar;
import org.sakaiproject.calendar.api.CalendarEventEdit;
import org.sakaiproject.calendaring.api.ExternalCalendaringServiceImpl;
import org.sakaiproject.time.api.Time;
import org.sakaiproject.time.api.TimeRange;
import org.sakaiproject.time.api.TimeService;

/**
 * Test class for the ExternalCalendaringService
 * @author Steve Swinsburg (Steve.swinsburg@gmail.com)
 *
 */
public class ExternalCalendaringServiceTest {

	private final String EVENT_NAME = "A new event";
	private final String LOCATION = "Building 1";
	private final String DESCRIPTION = "This is a sample event.";
	private final long START_TIME = 1336136400; // 4/May/2012 13:00 GMT
	private final long END_TIME = 1336140000; // 4/May/2012 14:00 GMT

	@InjectMocks ExternalCalendaringServiceImpl service = new ExternalCalendaringServiceImpl();
	
	/**
	 * Ensure the event generation works and returns a usable object
	 */
	@Test
	public void testGeneratingEvent() {
		
		CalendarEventEdit edit = new MockCalendarEventEdit();
		
		edit.setDisplayName(EVENT_NAME);
		edit.setLocation(LOCATION);
		edit.setDescription(DESCRIPTION);
		
		//TODO set more fields here
		
		TimeService timeService = new MockTimeService();
		Time start = timeService.newTime(START_TIME);
		Time end = timeService.newTime(END_TIME);
		TimeRange timeRange = timeService.newTimeRange(start, end, true, false);
		
		edit.setRange(timeRange);
		
		assertNotNull(edit);
				
		assertEquals(LOCATION, edit.getLocation());
		assertEquals(DESCRIPTION, edit.getDescription());
		assertEquals(EVENT_NAME, edit.getDisplayName());
		
	}

	@Test
	public void testGeneratingCalendar() {
		
		
		
	}
	
	
	

}
