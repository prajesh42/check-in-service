package com.siemens.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "EMPLOYEE")
@Data
public class Employee {
	
	@Id
	private String employeeId;
	private String name;	
	private String email;	
}
