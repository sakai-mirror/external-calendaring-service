package org.sakaiproject.calendaring.logic;

import javax.annotation.Resource;

import lombok.extern.apachecommons.CommonsLog;

import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;

/**
 * Implementation of our SakaiProxy API
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
@CommonsLog
public class SakaiProxyImpl implements SakaiProxy {
    
	/**
 	* {@inheritDoc}
 	*/
	public String getCurrentUserId() {
		return sessionManager.getCurrentSessionUserId();
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public boolean isSuperUser() {
		return securityService.isSuperUser();
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public String getServerName() {
		return serverConfigurationService.getServerName();
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public String getCalendarFilePath() {
		return serverConfigurationService.getString("calendar.path", "/tmp/");
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public String getUserEmail(String uuid) {
		String email = null;
		try {
			email = userDirectoryService.getUser(uuid).getEmail();
		} catch (UserNotDefinedException e) {
			log.warn("Cannot get email for id: " + uuid + " : " + e.getClass() + " : " + e.getMessage());
		}
		return email;
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public String getUserDisplayName(String uuid) {
	   String name = null;
		try {
			name = userDirectoryService.getUser(uuid).getDisplayName();
		} catch (UserNotDefinedException e) {
			log.warn("Cannot get displayname for id: " + uuid + " : " + e.getClass() + " : " + e.getMessage());
		}
		return name;
	}
	
	
	/**
	 * init
	 */
	public void init() {
		log.info("init");
	}
	
	
	@Resource(name="org.sakaiproject.tool.api.SessionManager")
	private SessionManager sessionManager;
	
	@Resource(name="org.sakaiproject.authz.api.SecurityService")
	private SecurityService securityService;
	
	@Resource(name="org.sakaiproject.component.api.ServerConfigurationService")
	private ServerConfigurationService serverConfigurationService;
	
	@Resource(name="org.sakaiproject.user.api.UserDirectoryService")
	private UserDirectoryService userDirectoryService;


}
