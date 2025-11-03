package com.siemens.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	
	/**
	 * @param toEmail
	 * @param hours
	 * This method sends email to the employee with a notification of worked hour.
	 */
	public void sendTrackedHours(String toEmail, Double hours) {
		SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(toEmail);
        emailMessage.setSubject("Your Worked Hours");
        emailMessage.setText("Hello! You worked " + hours + " hours today.");
        mailSender.send(emailMessage);
	}
}
