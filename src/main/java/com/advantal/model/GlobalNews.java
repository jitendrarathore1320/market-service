package com.advantal.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "GlobalNews")
public class GlobalNews {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//	private String uuid;
//	private String title;
//	private String description;
//	private String snippet;
//	private String url;
//	private String image_url;
//	private String published_at;
//	private String source;
//	private String type;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private String image_url;
	private String published;
	//chnge string to Timestamp
//	private Timestamp published_at;
	private String source;
	private String link;
	private String type;
	private String symbol;
	private String percentage;
	private String subType;
	private Date CreationDate;
	private Date updationDate;
}
