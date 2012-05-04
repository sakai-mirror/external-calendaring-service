package org.sakaiproject.calendaring;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.sakaiproject.time.api.Time;
import org.sakaiproject.time.api.TimeBreakdown;
import org.sakaiproject.time.api.TimeRange;
import org.sakaiproject.time.api.TimeService;

public class MockTimeService implements TimeService {

	@Override
	public Time newTime(long millis) {
		return new org.sakaiproject.mock.domain.Time(new Date(millis));
	}
	
	@Override
	public TimeRange newTimeRange(Time start, Time end, boolean startIncluded, boolean endIncluded) {
		return new MockTimeRange(start, end, startIncluded, endIncluded);
	}
	
	
	/* anything below here is not used at this stage */

	
	@Override
	public boolean clearLocalTimeZone(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean different(Time arg0, Time arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GregorianCalendar getCalendar(TimeZone arg0, int arg1, int arg2,
			int arg3, int arg4, int arg5, int arg6, int arg7) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeZone getLocalTimeZone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time newTime() {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public Time newTime(GregorianCalendar arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeBreakdown newTimeBreakdown(int arg0, int arg1, int arg2,
			int arg3, int arg4, int arg5, int arg6) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time newTimeGmt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time newTimeGmt(TimeBreakdown arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time newTimeGmt(int arg0, int arg1, int arg2, int arg3, int arg4,
			int arg5, int arg6) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time newTimeLocal(TimeBreakdown arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time newTimeLocal(int arg0, int arg1, int arg2, int arg3, int arg4,
			int arg5, int arg6) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeRange newTimeRange(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeRange newTimeRange(Time arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeRange newTimeRange(long arg0, long arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeRange newTimeRange(Time arg0, Time arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
