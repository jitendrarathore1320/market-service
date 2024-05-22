package com.advantal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.IndicesDayGraph;

@Repository
public interface IndicesDayGraphRepository extends JpaRepository<IndicesDayGraph, Long>{

	List<IndicesDayGraph> findBySymbol(String symbol);

	IndicesDayGraph findBySymbolAndDate(String symbol, String date);

	List<IndicesDayGraph> findBySymbolAndCountry(String symbol, String country);

	@Query(value = "SELECT * FROM indices_day_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 and fifteen.date >= ?3 ORDER BY fifteen.date DESC", countQuery = "SELECT * FROM indices_day_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 and fifteen.date >= ?3 ORDER BY fifteen.date DESC", nativeQuery = true)
	List<IndicesDayGraph> findAllSixMonthDaysData(String symbol, String country, String formattedPreviousDate);

	@Query(value = "SELECT * FROM indices_day_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 and fifteen.date >= ?3 ORDER BY fifteen.date DESC", countQuery = "SELECT * FROM indices_day_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 and fifteen.date >= ?3 ORDER BY fifteen.date DESC", nativeQuery = true)
	List<IndicesDayGraph> findAllOneYearMonthDaysData(String symbol, String country, String formattedPreviousDate);

	@Query(value = "SELECT * FROM indices_day_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 and fifteen.date >= ?3 ORDER BY fifteen.date DESC", countQuery = "SELECT * FROM indices_day_graph fifteen WHERE fifteen.symbol=?1 and fifteen.country=?2 and fifteen.date >= ?3 ORDER BY fifteen.date DESC", nativeQuery = true)
	List<IndicesDayGraph> findAllFiveYearMonthDaysData(String symbol, String country, String formattedPreviousDate);

}
