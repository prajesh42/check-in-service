package com.siemens.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPublisher {
	
//	@Value("${app.checkedin.event}")
//	private String topic;
	Logger log= LoggerFactory.getLogger(KafkaPublisher.class);

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	public void sendMessageToTopic(String message) {
		kafkaTemplate.send("checkedin-event",message);
		log.info("checkedin-event-sent",message);
	}
}
