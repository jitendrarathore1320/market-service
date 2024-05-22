package com.advantal.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.Holiday;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

	Holiday findByIdAndStatus(Long id, Short one);

	Holiday findByHolidayNameAndCountryIdFk(String holidayName, Long countryId);

	@Query(value = "SELECT * FROM holiday ho WHERE (ho.holiday_name like concat('%',?1,'%'))", countQuery = "SELECT count(*) from holiday ho WHERE (ho.holiday_name like concat('%',?1,'%'))", nativeQuery = true)
	Page<Holiday> searchHoliday(Pageable pageable, String keyword);

	Holiday findByHolidayName(String holidayName);

	Page<Holiday> findByCountry(Pageable pageable, String country);

	List<Holiday> findByCountryAndYear(String country, int year);

}
