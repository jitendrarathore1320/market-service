package com.advantal.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.IndicesMinuteGraph;

@Repository
public interface IndicesMinuteGraphRepository extends JpaRepository<IndicesMinuteGraph, Long>{

	List<IndicesMinuteGraph> findBySymbol(String symbol);

	IndicesMinuteGraph findBySymbolAndDate(String symbol, String date);
	
	@Query(value = "SELECT * FROM indices_minute_graph fifteen WHERE fifteen.symbol=?1 and fifteen.date <= ?2", countQuery = "SELECT * FROM indices_minute_graph fifteen WHERE fifteen.symbol=?1 and fifteen.date <= ?2", nativeQuery = true)
	List<IndicesMinuteGraph> findAllOneDayOldData(String symbol, String format);

	@Query(value = "SELECT * FROM indices_minute_graph fifteen WHERE fifteen.symbol=?1 and fifteen.date <= ?2", countQuery = "SELECT * FROM indices_minute_graph fifteen WHERE fifteen.symbol=?1 and fifteen.date <= ?2", nativeQuery = true)
	List<IndicesMinuteGraph> findAllOneMonthOldData(String symbol, LocalDateTime oneMonthAgoDateTime);

	@Query(value = "SELECT * FROM indices_minute_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 ORDER BY fifteen.date DESC", countQuery = "SELECT * FROM indices_minute_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 ORDER BY fifteen.date DESC", nativeQuery = true)
	List<IndicesMinuteGraph> findAllMinutesData(String symbol, String country);

	@Query(value = "SELECT * FROM indices_minute_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 and fifteen.date >= ?3 ORDER BY fifteen.date DESC", countQuery = "SELECT * FROM indices_minute_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 and fifteen.date >= ?3 ORDER BY fifteen.date DESC", nativeQuery = true)
	List<IndicesMinuteGraph> findAllFifteenDaysMinutesData(String symbol, String country, String formattedPreviousDate);

	@Query(value = "SELECT * FROM indices_minute_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 and fifteen.date >= ?3 ORDER BY fifteen.date DESC", countQuery = "SELECT * FROM indices_minute_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 and fifteen.date >= ?3 ORDER BY fifteen.date DESC", nativeQuery = true)
	List<IndicesMinuteGraph> findAllOneMonthMinuteData(String symbol, String country, String formattedPreviousDate);

}
