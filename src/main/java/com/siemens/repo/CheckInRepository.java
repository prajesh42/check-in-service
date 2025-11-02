package com.siemens.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.siemens.dto.CheckInDetails;
import com.siemens.dto.CheckInStatus;

public interface CheckInRepository extends JpaRepository<CheckInDetails, Long> {
	Optional<CheckInDetails> findByEmployeeIdAndStatus(String employeeId, CheckInStatus status);
}
