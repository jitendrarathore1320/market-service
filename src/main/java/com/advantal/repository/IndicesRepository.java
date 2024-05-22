package com.advantal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.Indices;

@Repository
public interface IndicesRepository extends JpaRepository<Indices, Long> {

	@Query(value = "SELECT * FROM indices ind WHERE ind.name like concat('%',?1,'%')", countQuery = "SELECT count(*) from indices ind WHERE ind.name like concat('%',?1,'%')", nativeQuery = true)
	Page<Indices> findAllIndicesUsingSearch(String keyWord, Pageable pageable);

	@Query(value = "SELECT * FROM indices ind WHERE ind.status=1 and ind.country =?1", countQuery = "SELECT count(*) from indices ind WHERE ind.status=1 and ind.country =?1", nativeQuery = true)
	Page<Indices> findAllIndices(String country, Pageable pageable);

	Indices findBySymbolAndExchange(String symbol, String exchange);

//	List<Indices> findByLastUpdatedMarketDataBeforeAndCountryAndStatus(Date olderDateObj, String country, Short status);

	List<Indices> findByCountryAndStatus(String country, Short one);

	Indices findByCountryAndSymbol(String currency, String symbol);

	Indices findByIdAndSymbol(Long id, String symbol);

	@Query(value = "SELECT * FROM indices ind WHERE ind.status=1", countQuery = "SELECT count(*) from indices ind WHERE ind.status=1", nativeQuery = true)
	Page<Indices> findAllIndices(Pageable pageable);

	Indices findBySymbol(String symbol);
	
	@Query(value = "SELECT * FROM indices ind WHERE ind.country=?1 and (ind.name like concat('%',?2,'%') or ind.country like concat('%',?2,'%'))", countQuery = "SELECT count(*) from indices ind WHERE ind.country=?1 and (ind.name like concat('%',?2,'%') or ind.country like concat('%',?2,'%'))", nativeQuery = true)
	Page<Indices> findAllIndicesWithCountryAndSearchingPagintion(String country, String keyword,Pageable pageable);

	@Query(value = "SELECT * FROM indices ind WHERE (ind.name like concat('%',?1,'%') or ind.country like concat('%',?1,'%'))", countQuery = "SELECT count(*) from indices ind WHERE (ind.name like concat('%',?1,'%') or ind.country like concat('%',?1,'%'))", nativeQuery = true)
	List<Indices> findAllIndicesUsingSearch(String keyWord);

	@Query(value = "SELECT * FROM indices ind WHERE ind.country=?1 and (ind.name like concat('%',?2,'%') or ind.country like concat('%',?2,'%'))", countQuery = "SELECT count(*) from indices ind WHERE ind.country=?1 and (ind.name like concat('%',?2,'%') or ind.country like concat('%',?2,'%'))", nativeQuery = true)
	List<Indices> findAllIndicesWithCountryAndSearching(String country, String keyword);

	@Query(value = "SELECT * FROM indices ind WHERE ind.status=1", countQuery = "SELECT count(*) from indices ind WHERE ind.status=1", nativeQuery = true)
	List<Indices> findAllIndices();

	@Query(value = "SELECT * FROM indices ind WHERE ind.status=1 And ind.country=?1", countQuery = "SELECT count(*) from indices ind WHERE ind.status=1 And ind.country=?1", nativeQuery = true)
	List<Indices> findAllIndicesWithCountry(String country);

}
