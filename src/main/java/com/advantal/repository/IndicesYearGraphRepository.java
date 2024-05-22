package com.advantal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.model.IndicesYearGraph;

@Repository
public interface IndicesYearGraphRepository extends JpaRepository<IndicesYearGraph, Long>{

	List<IndicesYearGraph> findBySymbol(String symbol);

	IndicesYearGraph findBySymbolAndDate(String symbol, String date);

	List<IndicesYearGraph> findBySymbolAndCountry(String symbol, String country);

}
