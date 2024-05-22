package com.advantal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.model.CryptoExchange;

@Repository
public interface CryptoExchangeRepository extends JpaRepository<CryptoExchange, Long>{

}
