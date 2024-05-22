package com.advantal.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ShareHolder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String symbol;

	private String country;

	private String exchange;

	private String shareHolderName;

	private Long shares;

	private Long sharesQtyChange;

	private String reportedDate;

	private Double percent_held;

	private Date creationDate;

	private Date updationDate;

	private Short status;

}
