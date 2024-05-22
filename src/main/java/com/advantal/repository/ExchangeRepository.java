package com.advantal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.Exchange;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long>{

	@Query(value = "SELECT * FROM exchange ex WHERE ex.country =?1", countQuery = "SELECT count(*) from exchange ex WHERE ex.country =?1", nativeQuery = true)
	Page<Exchange> findAllExchanges(String country, Pageable pageable);

//	@Query(value = "SELECT * FROM exchange ex WHERE ex.country =?1 and (ex.name like concat('%',?2,'%') or ex.code like concat('%',?2,'%'))", countQuery = "SELECT count(*) from exchange ex WHERE ex.country =?1 and (ex.name like concat('%',?2,'%') or ex.code like concat('%',?2,'%'))", nativeQuery = true)
//	Page<Exchange> findAllExchanges(String country, String keyWord, Pageable pageable);

	@Query(value = "SELECT * FROM exchange ex WHERE (ex.name like concat('%',?1,'%') or ex.country like concat('%',?1,'%'))", countQuery = "SELECT count(*) from exchange ex WHERE (ex.name like concat('%',?1,'%') or ex.country like concat('%',?1,'%'))", nativeQuery = true)
	Page<Exchange> findAllExchangeUsingSearch(String keyWord, Pageable pageable);

	Exchange findByCountryAndName(String country, String name);
	
	@Query(value = "SELECT * FROM exchange ex WHERE ex.country =?1", countQuery = "SELECT * FROM exchange ex WHERE ex.country =?1", nativeQuery = true)
	List<Exchange> findAllExchangesList(String country);
	
	@Query(value = "SELECT * FROM exchange ex WHERE ex.status !=0", countQuery = "SELECT count(*) from exchange ex WHERE ex.status !=0", nativeQuery = true)
	Page<Exchange> findAllExchanges(Pageable pageable);

	@Query(value = "SELECT * FROM exchange ex WHERE (ex.name like concat('%',?1,'%') or ex.country like concat('%',?1,'%'))", countQuery = "SELECT count(*) from exchange ex WHERE (ex.name like concat('%',?1,'%') or ex.country like concat('%',?1,'%'))", nativeQuery = true)
	List<Exchange> findAllExchangeUsingSearch(String country);

	@Query(value = "SELECT * FROM exchange ex WHERE ex.status !=0", countQuery = "SELECT count(*) from exchange ex WHERE ex.status !=0", nativeQuery = true)
	List<Exchange> findAllExchanges();

}
