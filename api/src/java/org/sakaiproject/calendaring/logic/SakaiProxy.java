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
	
	/**
	 * Get the name of the server
	 * @return
	 */
	public String getServerName();
	
	/**
	 * Get the configured calendar file path on the server. This is where the ICS files are created. Must have trailing slash.
	 * @return
	 */
	public String getCalendarFilePath();
	
	/**
	 * Get the email address for this user
	 * @param uuid
	 * @return
	 */
	public String getUserEmail(String uuid);
	
	/**
	 * Get the display name for this user
	 * @param uuid
	 * @return
	 */
	public String getUserDisplayName(String uuid);
}
