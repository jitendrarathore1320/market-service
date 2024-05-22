package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponseData {

	String uuid;
	String title;
	String description;
	String snippet;
	String url;
	String image_url;
	String published_at;
	String source;
	
}
