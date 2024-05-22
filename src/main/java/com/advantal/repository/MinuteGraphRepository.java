package com.advantal.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.MinuteGraph;

@Repository
public interface MinuteGraphRepository extends JpaRepository<MinuteGraph, Long>{

	List<MinuteGraph> findBySymbol(String symbol);

	MinuteGraph findBySymbolAndDate(String symbol, String date);

	@Query(value = "SELECT * FROM minute_graph one WHERE one.symbol=?1 and one.date <= ?2", countQuery = "SELECT * FROM minute_graph one WHERE one.symbol=?1 and one.date <= ?2", nativeQuery = true)
	List<MinuteGraph> findAllOneMonthOldData(String symbol, LocalDateTime oneMonthAgoDateTime);

}
