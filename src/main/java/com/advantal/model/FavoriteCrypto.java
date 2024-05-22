package com.advantal.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class FavoriteCrypto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	
//	private Long instrumentId;
	
	private String symbol;

	private String name;

	private String instrumentType;

	private Date creationDate;

	private Short status;

	@OneToOne(targetEntity = Crypto.class)
	@JoinColumn(name = "instrumentIdFk", referencedColumnName = "id")
	private Crypto crypto;

}
