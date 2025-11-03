package com.siemens.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.siemens.dto.FailedEvent;

public interface FailedEventRepository extends JpaRepository<FailedEvent, Long>  {

}
