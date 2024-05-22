package com.advantal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.ShortingKey;
@Repository
public interface ShortingKeyRepository extends JpaRepository<ShortingKey, Long>{

	@Query(value = "SELECT * FROM shorting_key sk WHERE sk.status=1 ORDER BY sk.short_by ASC",nativeQuery = true)
	List<ShortingKey> findAllShortingKey();
	
}
