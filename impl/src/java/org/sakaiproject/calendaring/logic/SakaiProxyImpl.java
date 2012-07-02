package org.sakaiproject.calendaring.logic;

import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import org.apache.commons.lang.StringUtils;
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
		
		if(StringUtils.isNotBlank(uuid)){
			try {
				return userDirectoryService.getUser(uuid).getEmail();
			} catch (UserNotDefinedException e) {
				log.warn("Cannot get email for id: " + uuid + " : " + e.getClass() + " : " + e.getMessage());
			}
		}
		return null;
	}
	
	/**
 	* {@inheritDoc}
 	*/
	public String getUserDisplayName(String uuid) {
		
		if(StringUtils.isNotBlank(uuid)){
			try {
				return userDirectoryService.getUser(uuid).getDisplayName();
			} catch (UserNotDefinedException e) {
				log.warn("Cannot get displayname for id: " + uuid + " : " + e.getClass() + " : " + e.getMessage());
			}
		}
		return null;
	}
	
	
	/**
	 * init
	 */
	public void init() {
		log.info("init");
	}
	

	@Setter
	private SessionManager sessionManager;
	
	@Setter	
	private SecurityService securityService;
	
	@Setter
	private ServerConfigurationService serverConfigurationService;
	
	@Setter
	private UserDirectoryService userDirectoryService;
	
}
