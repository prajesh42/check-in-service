package com.siemens.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siemens.service.CheckInService;

@RestController
public class CheckInController {
	
	@Autowired
	private CheckInService checkInService;
	
	@GetMapping
	public ResponseEntity<?> checkInAndOut(String employeeId) {
		checkInService.checkInAndOut(employeeId);
		return ResponseEntity.accepted().body(HttpStatus.ACCEPTED);
	}

}
