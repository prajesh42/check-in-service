package com.siemens.service;

public interface CheckInService {
	
	
	/**
	 * @param employeeId
	 * Service responsible for recording check in and check out time of the employee. It
	 * also sends the email to the employee on check out.
	 */
	public void checkInAndOut(String employeeId);
}
