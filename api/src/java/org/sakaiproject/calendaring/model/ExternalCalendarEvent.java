package org.sakaiproject.calendaring.model;

import java.io.File;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper class for handling external events
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalCalendarEvent implements Serializable {

	private String uuid;
	private File ics;
}
