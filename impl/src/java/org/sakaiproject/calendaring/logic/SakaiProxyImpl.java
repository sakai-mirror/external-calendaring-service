package org.sakaiproject.calendaring.logic;

import javax.annotation.Resource;

import lombok.extern.apachecommons.CommonsLog;

import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.tool.api.SessionManager;

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
	 * init
	 */
	public void init() {
		log.info("init");
	}
	
	
	@Resource(name="org.sakaiproject.tool.api.SessionManager")
	private SessionManager sessionManager;
	
	@Resource(name="org.sakaiproject.authz.api.SecurityService")
	private SecurityService securityService;
	
}
