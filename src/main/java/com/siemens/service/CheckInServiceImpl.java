package com.siemens.service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siemens.dto.CheckInDetails;
import com.siemens.dto.CheckInStatus;
import com.siemens.dto.Employee;
import com.siemens.repo.CheckInRepository;
import com.siemens.repo.EmployeeRepository;

@Service
public class CheckInServiceImpl implements CheckInService {

	@Autowired
	private CheckInRepository checkInRepo;
	@Autowired
	private EmployeeRepository empRepo;
	@Autowired
	private EmailService emailService;


	@Override
	public void checkInAndOut(String employeeId) {
		Optional<Employee> employee = empRepo.findById(employeeId);
		if(employee != null && !employee.isEmpty()) {
			Optional<CheckInDetails> checkInDetails = checkInRepo.findByEmployeeIdAndStatus(employeeId, CheckInStatus.CHECKED_IN);
			CheckInDetails details = checkInDetails.isEmpty()? null: checkInDetails.get();
			if(details != null && details.getStatus() == CheckInStatus.CHECKED_IN) {
				details.setCheckOutTime(OffsetDateTime.now());
				details.setStatus(CheckInStatus.CHECKED_OUT);
				Double hours = Duration.between(details.getCheckInTime(), details.getCheckOutTime()).toMinutes() / 60.0;
				details.setHours(hours);
				emailService.sendTrackedHours(employee.get().getEmail(), hours);
			} else {
				details = new CheckInDetails();
				details.setCheckInTime(OffsetDateTime.now());
				details.setEmployeeId(employeeId);
				details.setStatus(CheckInStatus.CHECKED_IN);
			}
			checkInRepo.save(details);
		}
	}
}
