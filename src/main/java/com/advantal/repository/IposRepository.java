package com.advantal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.Ipos;

@Repository
public interface IposRepository extends JpaRepository<Ipos, Long>{

	Ipos findBySymbolAndDate(String symbol, String date);

	@Query(value = "SELECT * FROM ipos ip WHERE ip.country=?1 And ip.status=?2 ORDER BY ip.date DESC",countQuery="SELECT * FROM ipos ip WHERE ip.country=?1 And ip.status=?2 ORDER BY ip.date DESC",nativeQuery = true)
	Page<Ipos> findAllIpos(String country, Short status, Pageable pageable);

}
