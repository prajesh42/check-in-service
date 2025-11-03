package com.siemens.kafka;

import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.dto.CheckInStatus;
import com.siemens.dto.FailedEvent;
import com.siemens.model.CheckInEvent;
import com.siemens.repo.FailedEventRepository;
import com.siemens.service.EmailService;
import com.siemens.service.LegacyService;

@Component
public class KafkaEventListener {

	Logger log= LoggerFactory.getLogger(KafkaEventListener.class);
	
	@Autowired
	private EmailService emailService;
	@Autowired
	private LegacyService legacyService;
	@Autowired
	private FailedEventRepository failedEventRepo;
	@Autowired
	private ObjectMapper mapper;
	
	@KafkaListener(topics = "checkedin-event", groupId = "kafkacheckinevent")
	public void consume(String message) {
		log.info("checkedin-event-received",message);
		try {
			CheckInEvent checkInEvent = mapper.readValue(message, CheckInEvent.class);
			if(checkInEvent.getType().equals(CheckInStatus.CHECKED_OUT.name())) {
				emailService.sendTrackedHours(checkInEvent.getEmployeeEmail(), checkInEvent.getHours());
				legacyService.recordEmployeeData(checkInEvent.getEmployeeId(), checkInEvent.getHours());
			}
		} catch (Exception e) {
			FailedEvent failedEvent = new FailedEvent();
			failedEvent.setPayload(message);
			failedEvent.setSentAt(OffsetDateTime.now());
			failedEventRepo.save(failedEvent);
			log.error("checkedin-event-error ::" + message);
			log.error("Error while processing checkin event", e);
		}
		
	}
}
