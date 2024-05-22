package com.advantal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.StockProfile;

@Repository
public interface StockProfileRepository extends JpaRepository<StockProfile, Long> {

	StockProfile findBySymbol(String symbol);

	@Query(value = "SELECT profile.sector FROM stock_profile profile WHERE profile.country =? GROUP BY profile.sector", nativeQuery = true)
	List<Object> getSectors(String country);

	StockProfile findBySymbolAndUpdationDateBefore(String symbol, Date olderDateObj);

}
