package com.advantal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.SecFiling;

@Repository
public interface SecFilingRepository extends JpaRepository<SecFiling, Long> {

	List<SecFiling> findBySymbol(String symbol);

	Page<SecFiling> findByCountryAndFilingType(Pageable pageable, String country, String filingType);

	List<SecFiling> findBySymbolAndFilingType(String symbol, String filingType);

	@Query(value = "SELECT * FROM sec_filing se WHERE country=?2 and (se.filing_type like concat('%',?1,'%') OR se.symbol like concat('%',?1,'%') OR se.country like concat('%',?1,'%'))", countQuery = "SELECT count(*) from sec_filing se WHERE country=?2 and (se.filing_type like concat('%',?1,'%') OR se.symbol like concat('%',?1,'%') OR se.country like concat('%',?1,'%'))", nativeQuery = true)
	Page<SecFiling> searchSecFiling(Pageable pageable, String keyWord, String country);

}
