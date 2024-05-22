package com.advantal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.Earning;
@Repository
public interface EarningRepository extends JpaRepository<Earning, Long>{

	Earning findBySymbolAndDate(String symbol, String date);

	@Query(value = "SELECT * FROM earning er WHERE er.country=?1 And er.status=?2 ORDER BY er.date DESC",countQuery="SELECT * FROM earning er WHERE er.country=?1 And er.status=?2 ORDER BY er.date DESC",nativeQuery = true)
	Page<Earning> findAllEarning(String country, Short status, Pageable pageable);

}
