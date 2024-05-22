package com.advantal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

	Country findByCountry(String country);

	Country findByCountryAndExchange(String country, String exchange);

	@Query(value = "SELECT * FROM country", countQuery = "SELECT * FROM country", nativeQuery = true)
	List<Country> findAllCountries();

}
