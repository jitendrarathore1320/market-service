package com.advantal.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String country;
	
	private String marketOpenTime;
	
	private String marketCloseTime;
	
	private String timeZone;
	
	private String exchange;
	
	private Integer intervalForUpdateInstrument;
	
	private String weekends;
		
	@OneToMany(targetEntity = Holiday.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "countryIdFk", referencedColumnName = "id")
	private List<Holiday> holidayList;
	
//	@OneToMany(targetEntity = Broker.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "countryIdFk", referencedColumnName = "id")
//	private List<Broker> brokersList;
	
}
