package com.advantal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.advantal.model.StockGainer;

@Repository
public interface StockGainerRepository extends JpaRepository<StockGainer, Long> {

	StockGainer findBySymbol(String symbol);

//	@Modifying
//    @Transactional
//	@Query(value = "TRUNCATE TABLE stock_gainer", nativeQuery = true)
//	void truncateTable();

	@Modifying
	@Transactional
	void deleteByCountry(String countryName);

	List<StockGainer> findByCountry(String saudiArabia);

	@Query(value = "SELECT count(*) FROM stock_gainer AS gainer WHERE gainer.country =?1", nativeQuery = true)
	Integer countByCountry(String country);

}
