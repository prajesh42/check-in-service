package com.siemens.dto;

import java.time.OffsetDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CHECK_IN_EVENT")
@Data
public class CheckInEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String type;
	private String relationId;
	private String employeeId;
	private String employeeEmail;
	private Double hours;
	private OffsetDateTime occuredAt;
	private OffsetDateTime sentAt;
	private boolean failStatus = false;
}
