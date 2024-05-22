package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementResponse {

	private Long id;
	private String title;
	private String description;
	private String image_url;
	private String published_at;
	private String source;
	private String link;
	private String symbol;
	private String percentage;
}
