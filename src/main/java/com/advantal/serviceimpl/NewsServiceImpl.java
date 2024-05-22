package com.advantal.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.advantal.model.GlobalNews;
import com.advantal.repository.NewsRepository;
import com.advantal.requestpayload.NewsRequestPayload;
import com.advantal.requestpayload.StockNewsRequestPayload;
import com.advantal.responsepayload.AllAnnouncementNewsResponse;
import com.advantal.responsepayload.AllNewsResponse;
import com.advantal.responsepayload.NewsResponse;
import com.advantal.responsepayload.NewsResponsePage;
import com.advantal.service.NewsService;
import com.advantal.utils.Constant;
import com.advantal.utils.ThirdPartyApiUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

	@Autowired
	ThirdPartyApiUtil thirdPartyApiUtil;

	@Autowired
	NewsRepository newsRepository;

	/* new implement code merge the all apis 05 oct */
	@Override
	public Map<String, Object> getAllNews(NewsRequestPayload newsRequestPayloadS) {
		Map<String, Object> response = new HashMap<String, Object>();
		AllNewsResponse newsResponsePage = new AllNewsResponse();
		AllAnnouncementNewsResponse announcementNewsPage = new AllAnnouncementNewsResponse();
		NewsResponsePage responsePage = new NewsResponsePage();
		Pageable pageable = null;
		Page<GlobalNews> page = null;
		Page<GlobalNews> page1 = null;
		List<NewsResponse> responseList = new ArrayList<NewsResponse>();
		List<GlobalNews> newsList = new ArrayList<GlobalNews>();
		List<NewsResponse> announcementResponseList = new ArrayList<NewsResponse>();
		List<GlobalNews> announcementNewsList = new ArrayList<GlobalNews>();
		try {
			pageable = PageRequest.of(newsRequestPayloadS.getPageIndex(), newsRequestPayloadS.getPageSize());
			if (newsRequestPayloadS.getPageSize() != null) {
				if (newsRequestPayloadS.getCountry().isBlank()) {
					/* news get without searching */
					page = newsRepository.findAllNews("Crypto", "news", pageable);
					if (page != null && !page.isEmpty()) {
						/* news get with searching */
						if (newsRequestPayloadS.getKeyword() != null && !newsRequestPayloadS.getKeyword().isBlank()) {
							page = newsRepository.findAllSearchingNews("Crypto", newsRequestPayloadS.getKeyword(),
									newsRequestPayloadS.getKeyword(), newsRequestPayloadS.getKeyword(), pageable);
							if (page != null) {
								newsList = page.getContent();
							} else {
								response.put(Constant.RESPONSE_CODE, Constant.OK);
								response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
								response.put(Constant.DATA, responsePage);
								log.info("record not found ! status - {}", Constant.OK);
							}
						} else {
							/* news get without searching */
							newsList = page.getContent();
						}
						for (GlobalNews news : newsList) {
							NewsResponse newsResponse = new NewsResponse();
							BeanUtils.copyProperties(news, newsResponse);
							if(news.getSource()!=null && !news.getSource().isBlank()) {
								if(news.getSource().contains("By")) {
									news.setSource(news.getSource());
								}else {
									news.setSource(Constant.BY+" "+ news.getSource());
								}
							}
							newsResponse.setPublished_at(news.getPublished());
							responseList.add(newsResponse);
						}
					} else {
						response.put(Constant.RESPONSE_CODE, Constant.OK);
						response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
						response.put(Constant.DATA, responsePage);
						log.info("record not found ! status - {}", Constant.OK);
					}
					/* Announcement News for Crypto */
					if (page1 == null) {
						response.put(Constant.RESPONSE_CODE, Constant.OK);
						response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
						response.put(Constant.DATA, responsePage);
						log.info("record not found ! status - {}", Constant.OK);
					}
				} else if (newsRequestPayloadS.getCountry().equals("USA")) {
					/* get all US news */
					page = newsRepository.findAllNews("USA", "news", pageable);
					if (!page.isEmpty() && page != null) {
						/* news get with searching */
						if (newsRequestPayloadS.getKeyword() != null && !newsRequestPayloadS.getKeyword().isBlank()) {
							/* news get with searching */
							page = newsRepository.findAllSearchingNews("USA", newsRequestPayloadS.getKeyword(),
									newsRequestPayloadS.getKeyword(), newsRequestPayloadS.getKeyword(), pageable);
							if (page != null) {
								newsList = page.getContent();
							} else {
								response.put(Constant.RESPONSE_CODE, Constant.OK);
								response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
								response.put(Constant.DATA, responsePage);
								log.info("record not found ! status - {}", Constant.OK);
							}
						} else {
							/* news get without searching */
							newsList = page.getContent();
						}
						for (GlobalNews news : newsList) {
							NewsResponse newsResponse = new NewsResponse();
							BeanUtils.copyProperties(news, newsResponse);
							if(news.getSource()!=null && !news.getSource().isBlank()) {
								if(news.getSource().contains("By")) {
									news.setSource(news.getSource());
								}else {
									news.setSource(Constant.BY+" "+ news.getSource());
								}
							}
							newsResponse.setPublished_at(news.getPublished());
							responseList.add(newsResponse);
						}
					} else {
						response.put(Constant.RESPONSE_CODE, Constant.OK);
						response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
						response.put(Constant.DATA, responsePage);
						log.info("record not found ! status - {}", Constant.OK);
					}
					/* Announcement News for USA */
					page1 = null;
					if (page1 == null) {
						response.put(Constant.RESPONSE_CODE, Constant.OK);
						response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
						response.put(Constant.DATA, responsePage);
						log.info("record not found ! status - {}", Constant.OK);
					}
				} else if (newsRequestPayloadS.getCountry().equals("KSA")) {
					/* get all KSA news */
					page = newsRepository.findAllNews("KSA", "news", pageable);
					if (!page.isEmpty() && page != null) {
						/* news get with searching */
						if (newsRequestPayloadS.getKeyword() != null && !newsRequestPayloadS.getKeyword().isBlank()) {
							/* news get with searching */
							page = newsRepository.findAllSearchingNews("KSA", newsRequestPayloadS.getKeyword(),
									newsRequestPayloadS.getKeyword(), newsRequestPayloadS.getKeyword(), pageable);
							if (page != null) {
								newsList = page.getContent();
							} else {
								response.put(Constant.RESPONSE_CODE, Constant.OK);
								response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
								response.put(Constant.DATA, responsePage);
								log.info("record not found ! status - {}", Constant.OK);
							}
						} else {
							/* news get without searching */
							newsList = page.getContent();
						}
						for (GlobalNews news : newsList) {
							NewsResponse newsResponse = new NewsResponse();
							BeanUtils.copyProperties(news, newsResponse);
							if(news.getSource()!=null && !news.getSource().isBlank()) {
								if(news.getSource().contains("By")) {
									news.setSource(news.getSource());
								}else {
									news.setSource(Constant.BY+" "+ news.getSource());
								}
							}
							newsResponse.setPublished_at(news.getPublished());
							responseList.add(newsResponse);
						}
					} else {
						response.put(Constant.RESPONSE_CODE, Constant.OK);
						response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
						response.put(Constant.DATA, responsePage);
						log.info("record not found ! status - {}", Constant.OK);
					}
					/* Announcement News for KSA */
					if (newsRequestPayloadS.getKeyword().isBlank()) {
						page1 = newsRepository.findAllNews("KSA", "AnnouncementNews", pageable);
					}
					if (page1 != null && !page1.isEmpty()) {
						announcementNewsList = page1.getContent();
						for (GlobalNews news : announcementNewsList) {
							NewsResponse newsResponse = new NewsResponse();
							BeanUtils.copyProperties(news, newsResponse);
							if(news.getSource()!=null && !news.getSource().isBlank()) {
								if(news.getSource().contains("By")) {
									news.setSource(news.getSource());
								}else {
									news.setSource(Constant.BY+" "+ news.getSource());
								}
							}
							newsResponse.setPublished_at(news.getPublished());
							announcementResponseList.add(newsResponse);
						}
					} else {
						response.put(Constant.RESPONSE_CODE, Constant.OK);
						response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
						response.put(Constant.DATA, responsePage);
						log.info("record not found ! status - {}", Constant.OK);
					}
				} else {
					response.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					response.put(Constant.MESSAGE, Constant.INVALID_COUNTRY);
					log.info("Invalid country. please enter the only US and SA country ! status - {}", Constant.OK);
				}
				/* set news response */
				newsResponsePage.setIsFirstPage(page.isFirst());
				newsResponsePage.setIsLastPage(page.isLast());
				newsResponsePage.setPageIndex(page.getNumber());
				newsResponsePage.setPageSize(page.getSize());
				newsResponsePage.setTotalElement(page.getTotalElements());
				newsResponsePage.setTotalPages(page.getTotalPages());
				newsResponsePage.setNewsResponseList(responseList);
				/* set announcement news response */
				if (page1 != null) {
					announcementNewsPage.setIsFirstPage(page1.isFirst());
					announcementNewsPage.setIsLastPage(page1.isLast());
					announcementNewsPage.setPageIndex(page1.getNumber());
					announcementNewsPage.setPageSize(page1.getSize());
					announcementNewsPage.setTotalElement(page1.getTotalElements());
					announcementNewsPage.setTotalPages(page1.getTotalPages());
					announcementNewsPage.setNewsResponseList(announcementResponseList);
				} else {
					announcementNewsPage = null;
				}
				/* set news & announcement news data in responsePage object */
				responsePage.setNewsResponse(newsResponsePage);
				responsePage.setAnnouncementNewsResponse(announcementNewsPage);
				response.put(Constant.RESPONSE_CODE, Constant.OK);
				response.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
				response.put(Constant.DATA, responsePage);
				log.info("record found ! status - {}", Constant.OK);

			} else {
				response.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				response.put(Constant.MESSAGE, Constant.PAGE_SIZE_MESSAGE);
				log.info("invalid page size. page size can't be null ! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			response.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.info("Exception! status - {}", e.getMessage());
		}
		return response;
	}

	/* stock news from (financialmodelingprep.com) FMPP */
	@Override
	public Map<String, Object> getStockNews(@Valid StockNewsRequestPayload stockNewsRequestPayload) {
		Map<String, Object> response = new HashMap<String, Object>();
		Pageable pageable = null;
		List<GlobalNews> stockNewsList = new ArrayList<GlobalNews>();
		List<NewsResponse> stockNewsResponseList = new ArrayList<NewsResponse>();
		AllNewsResponse newsResponsePage = new AllNewsResponse();
		NewsResponsePage responsePage = new NewsResponsePage();
		try {
			if (stockNewsRequestPayload.getPageSize() != 0) {
				pageable = PageRequest.of(stockNewsRequestPayload.getPageIndex(),
						stockNewsRequestPayload.getPageSize());
				Page<GlobalNews> stockNews = newsRepository.findAllStockNews(stockNewsRequestPayload.getCountry(),
						stockNewsRequestPayload.getSymbol(), pageable);
				if (stockNews != null && !stockNews.isEmpty()) {
					log.info(stockNewsRequestPayload.getSymbol()
							+ " this symbol are present in our news table ! status -{}", Constant.OK);
					stockNewsList = stockNews.getContent();
					for (GlobalNews globalNews : stockNewsList) {
						NewsResponse newsResponse = new NewsResponse();
						BeanUtils.copyProperties(globalNews, newsResponse);
						if(globalNews.getSource()!=null && !globalNews.getSource().isBlank()) {
							if(globalNews.getSource().contains("By")) {
								newsResponse.setSource(globalNews.getSource());
							}else {
								newsResponse.setSource(Constant.BY+" "+ globalNews.getSource());
							}
						}
						newsResponse.setPublished_at(globalNews.getPublished());
						stockNewsResponseList.add(newsResponse);
					}
					/* set news response */
					newsResponsePage.setIsFirstPage(stockNews.isFirst());
					newsResponsePage.setIsLastPage(stockNews.isLast());
					newsResponsePage.setPageIndex(stockNews.getNumber());
					newsResponsePage.setPageSize(stockNews.getSize());
					newsResponsePage.setTotalElement(stockNews.getTotalElements());
					newsResponsePage.setTotalPages(stockNews.getTotalPages());
					newsResponsePage.setNewsResponseList(stockNewsResponseList);
					/* set newsResponsePage response */
					responsePage.setNewsResponse(newsResponsePage);
					responsePage.setAnnouncementNewsResponse(null);
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
					response.put(Constant.DATA, responsePage);
					log.info(" stock news found successfully !! " + stockNews);
				} else {
					response.put(Constant.RESPONSE_CODE, Constant.OK);
					response.put(Constant.MESSAGE, Constant.DATA_NOT_FOUND_MESSAGE);
					response.put(Constant.DATA, stockNewsRequestPayload.getSymbol());
					log.info(stockNewsRequestPayload.getSymbol()
							+ " this symbol are not found in our news table ! status - {}", Constant.OK);
				}
			} else {
				response.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				response.put(Constant.MESSAGE, Constant.PAGE_SIZE_MESSAGE);
				log.info("invalid page size. page size can't be null ! status - {}", Constant.OK);
			}
			/* get USA & KSA Stocks news in our local database */
//        	List<GlobalNews> stockNews=newsRepository.findAllStockNews(stockNewsRequestPayload.getCountry(),stockNewsRequestPayload.getSymbol());

		} catch (Exception e) {
			response.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			response.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.info("Exception! status - {}", e.getMessage());
		}
		return response;
	}
}
