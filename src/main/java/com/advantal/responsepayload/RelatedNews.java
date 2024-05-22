package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatedNews {
	private String content;
	private String image_url;
	private String timestamp;
	private String title;
	private String url;
}
