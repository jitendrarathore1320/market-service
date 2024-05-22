package com.advantal.responsepayload;

import java.util.ArrayList;
import java.util.List;

import com.advantal.model.GlobalNews;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponsePage {

	AllNewsResponse newsResponse;
	
	AllAnnouncementNewsResponse announcementNewsResponse;
////
//	private Integer pageIndex;
////
//	private Integer pageSize;
////
//	private Long totalElement;
////add new param
//	private Integer totalPages;
//
//	private Boolean isLastPage;
//
//	private Boolean isFirstPage;
//
//	private List<NewsResponse> newsResponseList=new ArrayList<NewsResponse>();
//	
//	private List<AnnouncementNews> announcementResponseList=new ArrayList<AnnouncementNews>();

}
