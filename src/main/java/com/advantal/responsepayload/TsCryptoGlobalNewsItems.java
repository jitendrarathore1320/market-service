package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsCryptoGlobalNewsItems {

	String title;
	String content;
	String url;
	String 	image_url;
	String source_url;
	String timestamp;
}
