package org.sakaiproject.calendaring.logic;

/**
 * An interface to abstract all Sakai related API calls. This does not form part of the public API for the ExternalCalendaringService.
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
public interface SakaiProxy {

	/**
	 * Get current user id
	 * @return
	 */
	public String getCurrentUserId();
	
	/**
	 * Is the current user a superUser? (anyone in admin realm)
	 * @return
	 */
	public boolean isSuperUser();
	
}
