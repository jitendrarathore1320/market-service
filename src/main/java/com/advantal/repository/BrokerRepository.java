package com.advantal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.Broker;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, Long> {

	Broker findByIdAndStatus(Long id, Short one);

	@Query(value = "SELECT * FROM broker br WHERE br.type ='crypto'", nativeQuery = true)
	List<Broker> findAllCryptoExchange();

	Broker findByBroker(String exchangeName);

	@Query(value = "SELECT count(*) FROM broker br WHERE br.type ='crypto'", nativeQuery = true)
	Long countCryptoExchange();

}
