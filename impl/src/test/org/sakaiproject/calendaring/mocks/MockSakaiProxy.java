package org.sakaiproject.calendaring.mocks;

import org.sakaiproject.calendaring.logic.SakaiProxy;

/**
 * Mock of SakaiProxy so we can call the main service API
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
public class MockSakaiProxy implements SakaiProxy {

	
	@Override
	public String getCurrentUserId() {
		return "abc123";
	}

	@Override
	public boolean isSuperUser() {
		return false;
	}

	@Override
	public String getServerName() {
		return "server_xyz";
	}

	@Override
	public String getCalendarFilePath() {
		return "/tmp";
	}

}
