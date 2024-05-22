package com.advantal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.model.YearGraph;

@Repository
public interface YearGraphRepository extends JpaRepository<YearGraph, Long> {

	YearGraph findBySymbolAndDate(String symbol, String date);

	List<YearGraph> findBySymbol(String symbol);

}
