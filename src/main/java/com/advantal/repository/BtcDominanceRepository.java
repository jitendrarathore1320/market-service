package com.advantal.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.BtcDominance;

@Repository
public interface BtcDominanceRepository extends JpaRepository<BtcDominance, Long>{

	@Query(value ="SELECT * FROM btc_dominance btc WHERE btc.updation_date=?1",nativeQuery = true)
	BtcDominance getBtcUpdationDate(String date);

	@Query(value ="SELECT * FROM btc_dominance btc WHERE btc.updation_date>?1",nativeQuery = true)
	BtcDominance getUpdationDate(Date previousDate);

}
