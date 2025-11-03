package com.siemens.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.siemens.model.FeedEvent;

public interface CheckInService {
	
	
	/**
	 * @param employeeId
	 * This method is responsible for recording check in and check out time of the employee. It
	 * also sends the email to the employee on check out.
	 * @throws JsonProcessingException 
	 */
	public void checkInAndOut(String employeeId);
	
	
	/**
	 * @param feedEvent
	 * This method is responsible for processing and sending events to topic
	 * @throws JsonProcessingException
	 */
	public void processEvent(FeedEvent feedEvent);
}
