package com.advantal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.DayGraph;
@Repository
public interface DayGraphRepository extends JpaRepository<DayGraph, Long>{

	DayGraph findBySymbolAndDate(String symbol, String date);

	List<DayGraph> findBySymbol(String symbol);

	@Query(value = "SELECT * FROM day_graph day WHERE day.symbol=?1 and day.date <= ?2", countQuery = "SELECT * FROM day_graph day WHERE day.symbol=?1 and day.date <= ?2", nativeQuery = true)
	List<DayGraph> findAllFiveYearOldData(String symbol, String date);

}
