package com.siemens.service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.dto.CheckInDetails;
import com.siemens.dto.CheckInEvent;
import com.siemens.dto.CheckInStatus;
import com.siemens.dto.Employee;
import com.siemens.kafka.KafkaPublisher;
import com.siemens.model.FeedEvent;
import com.siemens.repo.CheckInRepository;
import com.siemens.repo.EmployeeRepository;

@Service
public class CheckInServiceImpl implements CheckInService {
	
	Logger log= LoggerFactory.getLogger(CheckInServiceImpl.class);

	@Autowired
	private CheckInRepository checkInRepo;
	@Autowired
	private EmployeeRepository empRepo;
	@Autowired
	private KafkaPublisher kafkaPublisher;
	@Autowired
	private ObjectMapper mapper;

	@Override
	public void checkInAndOut(String employeeId) {
		Optional<Employee> employee = empRepo.findById(employeeId);
		if(employee != null && !employee.isEmpty()) {
			Optional<CheckInDetails> checkInDetails = checkInRepo.findByEmployeeIdAndStatus(employeeId, CheckInStatus.CHECKED_IN);
			CheckInDetails details = checkInDetails.isEmpty()? null: checkInDetails.get();
			String empEmail = employee.get().getEmail();
			FeedEvent feedEvent = new FeedEvent();
			feedEvent.setEmployeeId(employeeId);
			feedEvent.setEmployeeEmail(empEmail);
			if(details != null && details.getStatus() == CheckInStatus.CHECKED_IN) {
				details.setCheckOutTime(OffsetDateTime.now());
				details.setStatus(CheckInStatus.CHECKED_OUT);
				Double hours = Duration.between(details.getCheckInTime(), details.getCheckOutTime()).toMinutes() / 60.0;
				details.setHours(hours);
				feedEvent.setType(CheckInStatus.CHECKED_OUT.name());
				feedEvent.setRelationId(details.getTraceId());
				feedEvent.setHours(hours);
			} else {
				details = new CheckInDetails();
				details.setCheckInTime(OffsetDateTime.now());
				details.setEmployeeId(employeeId);
				details.setStatus(CheckInStatus.CHECKED_IN);
				details.setTraceId(UUID.randomUUID().toString());
				feedEvent.setType(CheckInStatus.CHECKED_IN.name());
				feedEvent.setRelationId(details.getTraceId());
			}
			checkInRepo.save(details);
			processEvent(feedEvent);
		}
	}


	@Override
	public void processEvent(FeedEvent feedEvent) {
		CheckInEvent checkInEvent = new CheckInEvent();
		checkInEvent.setType(feedEvent.getType());
		checkInEvent.setRelationId(feedEvent.getRelationId());
		checkInEvent.setOccuredAt(OffsetDateTime.now());
		checkInEvent.setEmployeeId(feedEvent.getEmployeeId());
		checkInEvent.setEmployeeEmail(feedEvent.getEmployeeEmail());
		checkInEvent.setHours(feedEvent.getHours());
		
		try {
			String jsonEvent = mapper.writeValueAsString(checkInEvent);
			kafkaPublisher.sendMessageToTopic(jsonEvent);
		} catch (JsonProcessingException e) {
			log.error("Error while mapping event data", e);
		}
	}
}
