package com.example.smodytestdbbatch.repository;

import com.example.smodytestdbbatch.domain.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<Cycle, Long> {
}
