package com.siemens.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedEvent {

	private String type;
	private String relationId;
	private String employeeId;
	private String employeeEmail;
	private Double hours;
}
