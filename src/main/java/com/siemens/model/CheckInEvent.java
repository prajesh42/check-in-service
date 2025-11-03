package com.siemens.model;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class CheckInEvent {

	private Long id;
	private String type;
	private String relationId;
	private String employeeId;
	private String employeeEmail;
	private Double hours;
	private OffsetDateTime occuredAt;
}
