package com.advantal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.Dividend;
@Repository
public interface DividendRepository extends JpaRepository<Dividend, Long>{

	Dividend findBySymbolAndDate(String symbol, String date);

	@Query(value = "SELECT * FROM dividend dv WHERE dv.country=?1 And dv.status=?2 ORDER BY dv.date DESC",countQuery="SELECT * FROM dividend dv WHERE dv.country=?1 And dv.status=?2 ORDER BY dv.date DESC",nativeQuery = true)
	Page<Dividend> findAllDividend(String country, Short status, Pageable pageable);

}
