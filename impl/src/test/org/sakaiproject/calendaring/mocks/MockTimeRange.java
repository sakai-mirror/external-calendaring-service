package org.sakaiproject.calendaring.mocks;

import org.sakaiproject.time.api.Time;
import org.sakaiproject.time.api.TimeRange;

public class MockTimeRange implements TimeRange {

	private Time start;
	private Time end;
	private boolean startIncluded;
	private boolean endIncluded;
	
	
	public MockTimeRange(Time start, Time end, boolean startIncluded, boolean endIncluded) {
		this.start = start;
		this.end = end;
		this.startIncluded = startIncluded;
		this.endIncluded = endIncluded;
	}
	
	//required to fix visiblity error from Time
	public Object clone() {
		return new MockTimeRange(start, end, startIncluded, endIncluded);
	}
	
	@Override
	public Time firstTime() {
		return start;
	}
	
	@Override
	public Time lastTime() {
		return end;
	}
	
	/* anything below here is not used at this stage */
	
	
	@Override
	public void adjust(TimeRange arg0, TimeRange arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(Time arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(TimeRange arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long duration() {
		// TODO Auto-generated method stub
		return 0;
	}

	

	@Override
	public Time firstTime(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSingleTime() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Time lastTime(long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean overlaps(TimeRange arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shiftBackward(long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shiftForward(long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toStringHR() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void zoom(double arg0) {
		// TODO Auto-generated method stub
		
	}

}
