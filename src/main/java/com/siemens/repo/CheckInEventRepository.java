package com.siemens.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.siemens.dto.CheckInEvent;

public interface CheckInEventRepository extends JpaRepository<CheckInEvent, Long>  {

}
