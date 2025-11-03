package com.siemens.dto;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CHECK_IN_DETAILS")
@Data
public class CheckInDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private OffsetDateTime checkInTime;
	private OffsetDateTime checkOutTime;
	@Column(nullable = false)
	private CheckInStatus status;
	private Double hours;
	@Column(nullable = false)
	private String employeeId;
	@Column(nullable = false)
	private String traceId;
}
