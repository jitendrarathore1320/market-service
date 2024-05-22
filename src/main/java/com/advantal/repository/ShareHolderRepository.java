package com.advantal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.ShareHolder;

@Repository
public interface ShareHolderRepository extends JpaRepository<ShareHolder, Long> {

	List<ShareHolder> findBySymbolAndCountry(String symbol, String country);

	List<ShareHolder> findBySymbolAndExchange(String symbol, String exchange);

	@Query(value = "SELECT * FROM share_holder sh WHERE sh.symbol =?1 and sh.exchange=?2 and sh.status=1", countQuery = "SELECT * FROM share_holder sh WHERE sh.symbol =?1 and sh.exchange=?2 and sh.status=1", nativeQuery = true)
	Page<ShareHolder> findAllInstitutionalHolders(String symbol, String exchange, Pageable pageable);

}
