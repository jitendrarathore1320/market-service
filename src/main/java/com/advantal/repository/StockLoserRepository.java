package com.advantal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.advantal.model.StockLoser;

@Repository
public interface StockLoserRepository extends JpaRepository<StockLoser, Long> {

	StockLoser findBySymbol(String symbol);

//	@Modifying
//    @Transactional
//	@Query(value = "TRUNCATE TABLE stock_loser", nativeQuery = true)
//	void truncateTable();

	@Modifying
	@Transactional
	void deleteByCountry(String saudiArabia);

	List<StockLoser> findByCountry(String saudiArabia);

	@Query(value = "SELECT count(*) FROM stock_loser AS loser WHERE loser.country =?1", nativeQuery = true)
	Integer countByCountry(String country);
}
