package com.advantal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.model.KeyExecutive;

@Repository
public interface KeyExecutiveRepository extends JpaRepository<KeyExecutive, Long> {

	List<KeyExecutive> findBySymbolAndCountry(String symbol, String country);

	void deleteBySymbol(String symbol);

}
