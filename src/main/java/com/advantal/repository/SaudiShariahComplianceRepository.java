package com.advantal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.model.SaudiShariahCompliance;

@Repository
public interface SaudiShariahComplianceRepository extends JpaRepository<SaudiShariahCompliance, Long> {

	SaudiShariahCompliance findBySymbol(String tickerSymbol);

}
