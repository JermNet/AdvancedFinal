package com.example.firestation.repository;

import com.example.firestation.model.FireStation;
import org.springframework.data.jpa.repository.JpaRepository;
// Repository for FireStation
public interface FireStationRepository extends JpaRepository<FireStation, Long> {
}
