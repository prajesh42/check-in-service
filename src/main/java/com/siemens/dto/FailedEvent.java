package com.siemens.dto;

import java.time.OffsetDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CHECK_IN_EVENT")
@Data
@NoArgsConstructor
public class FailedEvent {

	@Id
	@GeneratedValue
	private Long id;
	private String payload;
	private OffsetDateTime sentAt;
}
