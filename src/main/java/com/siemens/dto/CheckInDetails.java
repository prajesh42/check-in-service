package com.siemens.dto;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CHECK_IN_DETAILS")
@Data
public class CheckInDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;
	public Date checkInTime;
	public Date checkOutTime;
	@ManyToOne
	@JoinColumn(name = "employeeId")
	public Employee employee;
}
