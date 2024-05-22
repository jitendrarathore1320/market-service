package com.advantal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.Economic;

@Repository
public interface EconomicRepository extends JpaRepository<Economic,Long>{

	Economic findByDateAndCountry(String date,String country);

	@Query(value = "SELECT * FROM economic ec WHERE ec.country=?1 And ec.status=?2 ORDER BY ec.date DESC",countQuery="SELECT * FROM economic ec WHERE ec.country=?1 And ec.status=?2 ORDER BY ec.date DESC",nativeQuery = true)
	Page<Economic> findAllEconomic(String country, Short status, Pageable pageable);

}
