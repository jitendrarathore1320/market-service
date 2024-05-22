package com.advantal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.Country;
import com.advantal.model.CsvFileType;
import com.advantal.model.FavoriteStock;

@Repository
public interface CsvFileRepository extends JpaRepository<CsvFileType, Long> {

	List<CsvFileType> findAll();

}