package com.advantal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.advantal.model.ShariaCompliance;

@Repository
public interface SariyaComplianceRepository extends JpaRepository<ShariaCompliance, Long> {

	@Modifying
	@Transactional
	void deleteByCountry(String country);

	@Query(value = "SELECT * FROM stock st LEFT JOIN saria_compliance sc ON sc.ticker_symbol = st.symbol WHERE st.country =? and st.status=1 ORDER BY sc.ticker_symbol ASC", countQuery = "SELECT count(*) from stock st WHERE st.country =?   and st.status=1", nativeQuery = true)
	Page<ShariaCompliance> filterBySariaCompliance(String country,String exchange, Pageable pageable);

	@Modifying
	@Transactional
	void deleteAllByCountry(String country);

	@Query(value = "SELECT * FROM sharia_compliance sc WHERE sc.country =?1 and sc.ticker_symbol=?2", nativeQuery = true)
	ShariaCompliance getStockByShariyaCompliance(String country, String tickerSymbol);

	@Query(value = "SELECT * FROM sharia_compliance sc WHERE sc.country =?1 and sc.ticker_symbol=?2", nativeQuery = true)
	ShariaCompliance findAllShariaComplianceDetails(String country, String tickerSymbol);

	@Query(value = "SELECT * FROM sharia_compliance sc WHERE sc.country =?1",countQuery = "SELECT * FROM sharia_compliance sc WHERE sc.country =?1", nativeQuery = true)
	List<ShariaCompliance> findCsvFile(String country);

}
