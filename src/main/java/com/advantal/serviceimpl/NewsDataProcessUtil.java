package com.advantal.serviceimpl;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advantal.model.Country;
import com.advantal.model.GlobalNews;
import com.advantal.model.Indices;
import com.advantal.model.Stock;
import com.advantal.repository.IndicesRepository;
import com.advantal.repository.NewsRepository;
import com.advantal.repository.StockRepository;
import com.advantal.responsepayload.AnnouncementNewsResponse;
import com.advantal.responsepayload.AnnouncementRobotsItems;
import com.advantal.responsepayload.CryptoNewsResponse;
import com.advantal.responsepayload.IndicesRobotItems;
import com.advantal.responsepayload.RobotsItems;
import com.advantal.responsepayload.SaRobotsItems;
import com.advantal.responsepayload.SaudiMarketNews;
import com.advantal.responsepayload.SaudiNewsResponse;
import com.advantal.responsepayload.StockNews;
import com.advantal.responsepayload.SukukIndicesResponse;
import com.advantal.responsepayload.UsNewsResponse;
import com.advantal.responsepayload.UsRobotsItems;
import com.advantal.utils.Constant;
import com.advantal.utils.DateUtil;
import com.advantal.utils.ThirdPartyApiUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NewsDataProcessUtil {

	@Autowired
	private ThirdPartyApiUtil thirdPartyApiUtil;

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private IndicesRepository indicesRepository;

	/* browse AI */
	public Integer currentPage(Integer remainder, Integer currentPage) {
		Integer remainder1 = remainder % 10;
		Integer currentPage1 = currentPage / 10;
		if (remainder1 == 0) {
			currentPage1 = currentPage1;
		} else {
			currentPage1++;
		}
		return currentPage1;
	}

//	public String saveNews() {
//		Integer lenght, currentPage = 1, usaCurrentPage = 1, cryptoCurrentPage = 1, announcementCurrentPage = 1,
//				count = 0;
//		String response = "";
//		try {
//			/* saudi news */
//			String strResponse = thirdPartyApiUtil.getAllSaudiNews("ksa", currentPage);
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			if (!strResponse.isBlank()) {
//				Map<?, ?> mapResponse = mapper.readValue(strResponse, Map.class);
//				SaudiNewsResponse ksanews = mapper.convertValue(mapResponse, SaudiNewsResponse.class);
//				if (ksanews.getStatusCode().equals("200") && ksanews.getMessageCode().equals("success")) {
//					currentPage = currentPage(ksanews.getResult().getRobotTasks().getTotalCount(),
//							ksanews.getResult().getRobotTasks().getTotalCount());
//				}
//				String strResponse1 = thirdPartyApiUtil.getAllSaudiNews("ksa", currentPage);
//				ObjectMapper mapper1 = new ObjectMapper();
//				mapper1.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//				if (!strResponse1.isBlank()) {
//					Map<?, ?> mapResponse1 = mapper1.readValue(strResponse1, Map.class);
//					SaudiNewsResponse ksanews1 = mapper1.convertValue(mapResponse1, SaudiNewsResponse.class);
//					if (ksanews1.getStatusCode().equals("200") && ksanews1.getMessageCode().equals("success")) {
//						SaRobotsItems robotsItems = new SaRobotsItems();
//						log.info("size:- " + ksanews1.getResult().getRobotTasks().getItems().size());
//						lenght = ksanews1.getResult().getRobotTasks().getItems().size() - 1;
//						log.info("Items lenght :- " + lenght);
//						/* deleted old Saudi news */
//						BeanUtils.copyProperties(ksanews1.getResult().getRobotTasks().getItems().get(lenght),
//								robotsItems);
//						log.info("Item :- " + robotsItems);
//						/* Saudi news updated */
//						for (SaudiMarketNews items : robotsItems.getCapturedLists().getSaudinewsenglish()) {
//							if (!items.get_STATUS().equals("REMOVED")) {
//								GlobalNews oldnews = newsRepository.findByTitleAndPublishedAndTypeAndSubType(
//										items.getTitle(), DateUtil.changeStringaDateFormat(items.getDate()), "KSA",
//										"news");
//								if (oldnews == null) {
//									count++;
//									GlobalNews globalNews = new GlobalNews();
//									globalNews.setTitle(items.getTitle());
//									globalNews.setDescription(items.getDescription());
//									globalNews.setLink(items.getLink());
//									globalNews.setPublished(DateUtil.changeStringaDateFormat(items.getDate()));
//									globalNews.setSource("www.saudiexchange.sa");
//									globalNews.setType("KSA");
//									globalNews.setSubType("news");
//									globalNews.setUpdationDate(new Date());
//									newsRepository.save(globalNews);
//									response = "saudi news save successfully !!";
//									log.info(count + " saudi news save successfully" + "! status - {}", Constant.OK);
//								} else {
//									oldnews.setUpdationDate(new Date());
//									newsRepository.save(oldnews);
//									log.info("this saudi news alreay found in our database can't saved");
//								}
//							}
//						}
//					} else {
//						log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//						response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//					}
//				} else {
//					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//					response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//				}
//			} else {
//				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//				response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//			}
//			/* usa news */
//			String strResponse1 = thirdPartyApiUtil.getAllUsaNews("usa", usaCurrentPage);
//			ObjectMapper mapper1 = new ObjectMapper();
//			mapper1.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			if (!strResponse1.isBlank()) {
//				int usacount = 0;
//				Map<?, ?> mapResponse = mapper1.readValue(strResponse1, Map.class);
//				UsNewsResponse usanews = mapper1.convertValue(mapResponse, UsNewsResponse.class);
//				if (usanews.getStatusCode().equals("200") && usanews.getMessageCode().equals("success")) {
//					usaCurrentPage = currentPage(usanews.getResult().getRobotTasks().getTotalCount(),
//							usanews.getResult().getRobotTasks().getTotalCount());
//				}
//				String strResponse2 = thirdPartyApiUtil.getAllUsaNews("usa", usaCurrentPage);
//				ObjectMapper mapper2 = new ObjectMapper();
//				mapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//				if (!strResponse2.isBlank()) {
//					Map<?, ?> mapResponse1 = mapper2.readValue(strResponse2, Map.class);
//					UsNewsResponse usanews1 = mapper2.convertValue(mapResponse1, UsNewsResponse.class);
//					if (usanews1.getStatusCode().equals("200") && usanews1.getMessageCode().equals("success")) {
//						UsRobotsItems robotsItems = new UsRobotsItems();
//						log.info("size:- " + usanews1.getResult().getRobotTasks().getItems().size());
//						lenght = usanews1.getResult().getRobotTasks().getItems().size() - 1;
//						log.info("Items lenght :- " + lenght);
//						BeanUtils.copyProperties(usanews1.getResult().getRobotTasks().getItems().get(lenght),
//								robotsItems);
//						log.info("Item :- " + robotsItems);
//						for (SaudiMarketNews items : robotsItems.getCapturedLists().getUsnewsenglish()) {
//							/* Remove data not added in our database */
//							if (!items.get_STATUS().equals("REMOVED")) {
//								GlobalNews news = newsRepository.findByTitleAndTypeAndSubType(items.getTitle(), "USA",
//										"news");
//								if (news == null) {
//									usacount++;
//									GlobalNews globalNews = new GlobalNews();
//									globalNews.setTitle(items.getTitle());
//									globalNews.setDescription(items.getDescription());
//									globalNews.setLink(items.getLink());
//									globalNews.setPublished(DateUtil.MinToStringDateTimeFormat(items.getDate()));
//									globalNews.setSource(items.getProvider());
//									globalNews.setImage_url(items.getImage());
//									globalNews.setType("USA");
//									globalNews.setSubType("news");
//									globalNews.setUpdationDate(new Date());
//									newsRepository.save(globalNews);
//									response = "USA news save successfully !!";
//									log.info(usacount + " USA news save successfully" + "! status - {}", Constant.OK);
//								} else {
//									news.setUpdationDate(new Date());
//									newsRepository.save(news);
//									log.info("this USA news alreay found in our database can't saved");
//								}
//							}
//						}
//					} else {
//						log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//						response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//					}
//				} else {
//					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//					response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//				}
//			} else {
//				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//				response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//			}
//			/* crypto news */
//			String cryptoResponse = thirdPartyApiUtil.getCryptoNews(cryptoCurrentPage);
//			ObjectMapper mapper3 = new ObjectMapper();
//			mapper3.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//			if (!cryptoResponse.isBlank()) {
//				int cryptocount = 0;
//				Map<?, ?> mapResponse = mapper3.readValue(cryptoResponse, Map.class);
//				UsNewsResponse cryptonews = mapper3.convertValue(mapResponse, UsNewsResponse.class);
//				if (cryptonews.getStatusCode().equals("200") && cryptonews.getMessageCode().equals("success")) {
//					cryptoCurrentPage = currentPage(cryptonews.getResult().getRobotTasks().getTotalCount(),
//							cryptonews.getResult().getRobotTasks().getTotalCount());
//				}
//				String cryptoResponse1 = thirdPartyApiUtil.getCryptoNews(cryptoCurrentPage);
//				ObjectMapper mapper4 = new ObjectMapper();
//				mapper4.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//				if (!cryptoResponse1.isBlank()) {
//					Map<?, ?> mapResponse1 = mapper4.readValue(cryptoResponse1, Map.class);
//					CryptoNewsResponse cryptonews1 = mapper4.convertValue(mapResponse1, CryptoNewsResponse.class);
//					if (cryptonews1.getStatusCode().equals("200") && cryptonews1.getMessageCode().equals("success")) {
//						RobotsItems robotsItems = new RobotsItems();
//						log.info("size:- " + cryptonews1.getResult().getRobotTasks().getItems().size());
//						lenght = cryptonews1.getResult().getRobotTasks().getItems().size() - 1;
//						log.info("Items lenght :- " + lenght);
//						BeanUtils.copyProperties(cryptonews1.getResult().getRobotTasks().getItems().get(lenght),
//								robotsItems);
//						log.info("Item :- " + robotsItems);
//						for (SaudiMarketNews items : robotsItems.getCapturedLists().getCryptonews()) {
//							if (!items.get_STATUS().equals("REMOVED")) {
//								GlobalNews globalNews1 = newsRepository.findByTitleAndTypeAndSubType(items.getTitle(),
//										"Crypto", "news");
//								if (globalNews1 == null) {
//									cryptocount++;
//									GlobalNews globalNews = new GlobalNews();
//									globalNews.setTitle(items.getTitle());
//									globalNews.setDescription(items.getDescription());
//									globalNews.setLink(items.getLink());
//									globalNews.setPublished(DateUtil.MinToStringDateTimeFormat(items.getDate()));
//									globalNews.setSource(items.getProvider());
//									globalNews.setImage_url(items.getImage());
//									globalNews.setType("Crypto");
//									globalNews.setSubType("news");
//									globalNews.setUpdationDate(new Date());
//									newsRepository.save(globalNews);
//									response = "Crypto news save successfully !!";
//									log.info(cryptocount + " Crypto news save successfully" + "! status - {}",
//											Constant.OK);
//								} else {
//									globalNews1.setUpdationDate(new Date());
//									newsRepository.save(globalNews1);
//									log.info("this Crypto news alreay found in our database can't saved");
//								}
//							}
//						}
//					} else {
//						log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//						response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//					}
//				} else {
//					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//					response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//				}
//			} else {
//				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//				response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//			}
//			/* announcement news */
//			String announcementnewsresponse = thirdPartyApiUtil.getAllAnnouncementNews(announcementCurrentPage);
//			ObjectMapper announcementmapper = new ObjectMapper();
//			announcementmapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			if (!announcementnewsresponse.isBlank()) {
//				int newscount = 0;
//				Map<?, ?> mapResponse = announcementmapper.readValue(announcementnewsresponse, Map.class);
//				AnnouncementNewsResponse announcementnews = announcementmapper.convertValue(mapResponse,
//						AnnouncementNewsResponse.class);
//				if (announcementnews.getStatusCode().equals("200")
//						&& announcementnews.getMessageCode().equals("success")) {
//					announcementCurrentPage = currentPage(announcementnews.getResult().getRobotTasks().getTotalCount(),
//							announcementnews.getResult().getRobotTasks().getTotalCount());
//					String announcementnewsresponse1 = thirdPartyApiUtil
//							.getAllAnnouncementNews(announcementCurrentPage);
//					ObjectMapper announcementmapper1 = new ObjectMapper();
//					announcementmapper1.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//					if (!announcementnewsresponse1.isBlank()) {
//						Map<?, ?> mapResponse1 = announcementmapper1.readValue(announcementnewsresponse1, Map.class);
//						AnnouncementNewsResponse announcementnews1 = announcementmapper1.convertValue(mapResponse1,
//								AnnouncementNewsResponse.class);
//						if (announcementnews1.getStatusCode().equals("200")
//								&& announcementnews1.getMessageCode().equals("success")) {
//							AnnouncementRobotsItems robotsItems = new AnnouncementRobotsItems();
//							log.info("size:- " + announcementnews1.getResult().getRobotTasks().getItems().size());
//							lenght = announcementnews1.getResult().getRobotTasks().getItems().size() - 1;
//							log.info("Items lenght :- " + lenght);
//							BeanUtils.copyProperties(
//									announcementnews1.getResult().getRobotTasks().getItems().get(lenght), robotsItems);
//							log.info("Item :- " + robotsItems);
//							for (SaudiMarketNews items : robotsItems.getCapturedLists().getAnnouncementnews()) {
//								if (!items.get_STATUS().equals("REMOVED")) {
//									int length;
//									length = items.getDate().length();
//									String datetime = items.getDate().substring(length - 19);
//									GlobalNews globalNews1 = newsRepository.findByTitleAndPublishedAndTypeAndSubType(
//											items.getTitle(), DateUtil.changeStringaDateFormat(datetime), "KSA",
//											"AnnouncementNews");
//									if (globalNews1 == null) {
//										GlobalNews globalNews = new GlobalNews();
//										newscount++;
//										globalNews.setTitle(items.getTitle());
//										globalNews.setDescription(items.getDescription());
//										globalNews.setLink(items.getLink());
//										globalNews.setPublished(DateUtil.changeStringaDateFormat(datetime));
//										System.out.println(DateUtil.changeStringaDateFormat(datetime));
//										globalNews.setSource("www.saudiexchange.sa");
//										globalNews.setPercentage(items.getPercentage());
//										globalNews.setType("KSA");
//										globalNews.setSymbol(items.getSymbol());
//										globalNews.setSubType("AnnouncementNews");
//										globalNews.setUpdationDate(new Date());
//										Stock stock = stockRepository.findBySymbol(items.getSymbol());
//										/* set announcement news logo */
//										if (stock != null) {
//											globalNews.setImage_url(stock.getLogo());
//											response = "Announcement news updated successfully !!";
//											log.info(newscount + " Announcement news updated successfully"
//													+ "! status - {}", Constant.OK);
//										} else {
//											log.info(items.getSymbol()
//													+ " this stock are not found in our database. you can't save logo !!");
//										}
//										newsRepository.save(globalNews);
//										response = "Announcement news updated successfully !!";
//										log.info(
//												newscount + " Announcement news updated successfully" + "! status - {}",
//												Constant.OK);
//									} else {
//										Stock stock = stockRepository.findBySymbol(globalNews1.getSymbol());
//										globalNews1.setUpdationDate(new Date());
//										/* set announcement news logo */
//										if (stock != null) {
//											log.info("this Announcement news alreay found in our database can't saved");
//											globalNews1.setImage_url(stock.getLogo());
//											response = "Announcement news updated successfully !!";
//											log.info(globalNews1.getSymbol() + " this news logo saved succesfully !!");
//											log.info(newscount + " Announcement news updated successfully"
//													+ "! status - {}", Constant.OK);
//										} else {
//											log.info(globalNews1.getSymbol()
//													+ " this stock are not found in our database. you can't save logo !!");
//										}
//										newsRepository.save(globalNews1);
//									}
//								}
//							}
//							/* deleted before 1 month announcement news from our database */
//							System.out.println(DateUtil.currentDateTimeToOneMonth());
//							List<GlobalNews> oldsaudinews = newsRepository
//									.findByPublishedBefore(DateUtil.currentToOneMonth());
//							if (oldsaudinews != null && !oldsaudinews.isEmpty()) {
//								newsRepository.deleteAll(oldsaudinews);
//								log.info("Before 1 month all Old" + oldsaudinews.size()
//										+ " news deleted succesfully ! status - {}", Constant.OK);
//							} else {
//								log.info("Befor 1 month old " + oldsaudinews.size() + " news not found ! status - {}",
//										Constant.OK);
//							}
//						} else {
//							log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//							response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//						}
//					} else {
//						log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//						response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//					}
//				}
//			} else {
//				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
//				response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
//			}
//		} catch (Exception e) {
//			log.info("Exception! status - {}", e.getMessage());
//			response = "Exception! status - {}" + e.getMessage();
//		}
//		return response;
//	}

	/* Brows AI News Data process */

	public class IndicesFromBrowsAiTask implements Runnable {
		@Override
		public void run() {
			saveIndicesFromBrowsAi();
		}
	}

	public void IndicesFromBrowsThread() {
		IndicesFromBrowsAiTask indicesFromBrowsAiTask = new IndicesFromBrowsAiTask();
		Thread thread = new Thread(indicesFromBrowsAiTask);
		thread.start();
	}

	public class NewsFromBrowsAiTask implements Runnable {
		@Override
		public void run() {
			saveGlobalNewsFromBrowsAi();
		}
	}

	public void newsFormFmpThread() {
		NewsFromBrowsAiTask newsFromBrowsAiTask = new NewsFromBrowsAiTask();
		Thread thread = new Thread(newsFromBrowsAiTask);
		thread.start();
	}

	public String saveIndicesFromBrowsAi() {
		Integer currentPage = 1;
		String response = "";
		try {
			String strResponse = thirdPartyApiUtil.getIndices(currentPage);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (!strResponse.isBlank()) {
				Map<?, ?> mapResponse = mapper.readValue(strResponse, Map.class);
				SukukIndicesResponse sukukIndicesResponse = mapper.convertValue(mapResponse,
						SukukIndicesResponse.class);
				if (sukukIndicesResponse.getStatusCode().equals("200")
						&& sukukIndicesResponse.getMessageCode().equals("success")) {
					currentPage = currentPage(sukukIndicesResponse.getResult().getRobotTasks().getTotalCount(),
							sukukIndicesResponse.getResult().getRobotTasks().getTotalCount());
				}
				/* indices method called */
				getIndices(currentPage);
			} else {
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
			}
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
		return response;
	}

	private void getIndices(Integer currentPage) {

		Integer lenght, count = 0;
		try {
			String strResponse1 = "";
			ObjectMapper mapper1 = new ObjectMapper();
			mapper1.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			strResponse1 = thirdPartyApiUtil.getIndices(currentPage);
			if (!strResponse1.isBlank()) {
				Map<?, ?> mapResponse1 = mapper1.readValue(strResponse1, Map.class);
				SukukIndicesResponse sukukIndicesResponse = mapper1.convertValue(mapResponse1,
						SukukIndicesResponse.class);
				if (sukukIndicesResponse.getStatusCode().equals("200")
						&& sukukIndicesResponse.getMessageCode().equals("success")) {
					IndicesRobotItems indicesRobotItems = new IndicesRobotItems();
					log.info("find the lenght inside the robot in task size:- "
							+ sukukIndicesResponse.getResult().getRobotTasks().getItems().size());
					lenght = sukukIndicesResponse.getResult().getRobotTasks().getItems().size() - 1;
					log.info("get the items task lenght :- " + lenght);
					BeanUtils.copyProperties(sukukIndicesResponse.getResult().getRobotTasks().getItems().get(lenght),
							indicesRobotItems);
					log.info("Data copy third party to local class :- " + indicesRobotItems);
					/*------ Indices data added --------*/
					if (indicesRobotItems != null && indicesRobotItems.getCapturedTexts() != null) {
						Indices indices = indicesRepository.findBySymbolAndExchange("TSBI", "Tadawul");
						if (indices != null) {
//							indices.setName(indicesRobotItems.getCapturedTexts().getSymbol());
							indices.setClose(indicesRobotItems.getCapturedTexts().getPrice());
							String[] value_percentage = indicesRobotItems.getCapturedTexts()
									.getValue_percentage_change().split(" ");
							indices.setPrice_change(value_percentage[0]);
							StringBuilder stringBuilder = new StringBuilder();
							stringBuilder.append(value_percentage[1]);
							log.info(" length :-" + stringBuilder.length());
							stringBuilder.deleteCharAt(stringBuilder.length() - 1);
							log.info(" length :-" + stringBuilder.length());
							stringBuilder.deleteCharAt(stringBuilder.length() - 1);
							stringBuilder.deleteCharAt(0);
							indices.setPercent_change(stringBuilder.toString());
							indices.setLastUpdatedMarketData(new Date());
							indicesRepository.save(indices);
							log.info(count + " Sukuk indices data save successfully" + "! status - {}", Constant.OK);
						}
					} else {
						log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
					}
				} else {
					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}

	}

	public String saveGlobalNewsFromBrowsAi() {
		Integer currentPage = 1, usaCurrentPage = 1, cryptoCurrentPage = 1, announcementCurrentPage = 1;
		String response = "";
		try {
			/* saudi news */
			String strResponse = thirdPartyApiUtil.getAllSaudiNews("ksa", currentPage);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (!strResponse.isBlank()) {
				Map<?, ?> mapResponse = mapper.readValue(strResponse, Map.class);
				SaudiNewsResponse ksanews = mapper.convertValue(mapResponse, SaudiNewsResponse.class);
				if (ksanews.getStatusCode().equals("200") && ksanews.getMessageCode().equals("success")) {
					currentPage = currentPage(ksanews.getResult().getRobotTasks().getTotalCount(),
							ksanews.getResult().getRobotTasks().getTotalCount());
				}
				/* saudi news method called */
				saudiNews(currentPage);
			} else {
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
			}
			/* usa news */
			String strResponse1 = thirdPartyApiUtil.getAllUsaNews("usa", usaCurrentPage);
			ObjectMapper mapper1 = new ObjectMapper();
			mapper1.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (!strResponse1.isBlank()) {
				Map<?, ?> mapResponse = mapper1.readValue(strResponse1, Map.class);
				UsNewsResponse usanews = mapper1.convertValue(mapResponse, UsNewsResponse.class);
				if (usanews.getStatusCode().equals("200") && usanews.getMessageCode().equals("success")) {
					usaCurrentPage = currentPage(usanews.getResult().getRobotTasks().getTotalCount(),
							usanews.getResult().getRobotTasks().getTotalCount());
				}
				/* usa news method called */
				usaNews(usaCurrentPage);
			} else {
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
			}
			/* crypto news */
			String cryptoResponse = thirdPartyApiUtil.getCryptoNews(cryptoCurrentPage);
			ObjectMapper mapper3 = new ObjectMapper();
			mapper3.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (!cryptoResponse.isBlank()) {
				Map<?, ?> mapResponse = mapper3.readValue(cryptoResponse, Map.class);
				UsNewsResponse cryptonews = mapper3.convertValue(mapResponse, UsNewsResponse.class);
				if (cryptonews.getStatusCode().equals("200") && cryptonews.getMessageCode().equals("success")) {
					cryptoCurrentPage = currentPage(cryptonews.getResult().getRobotTasks().getTotalCount(),
							cryptonews.getResult().getRobotTasks().getTotalCount());
				}
				/* crypto new method called */
				crytpoNews(cryptoCurrentPage);
			} else {
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
			}
			/* announcement news */
			String announcementnewsresponse = thirdPartyApiUtil.getAllAnnouncementNews(announcementCurrentPage);
			ObjectMapper announcementmapper = new ObjectMapper();
			announcementmapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (!announcementnewsresponse.isBlank()) {
				Map<?, ?> mapResponse = announcementmapper.readValue(announcementnewsresponse, Map.class);
				AnnouncementNewsResponse announcementnews = announcementmapper.convertValue(mapResponse,
						AnnouncementNewsResponse.class);
				if (announcementnews.getStatusCode().equals("200")
						&& announcementnews.getMessageCode().equals("success")) {
					announcementCurrentPage = currentPage(announcementnews.getResult().getRobotTasks().getTotalCount(),
							announcementnews.getResult().getRobotTasks().getTotalCount());
					/* announcement news method called */
					announcementNews(announcementCurrentPage);
				}
			} else {
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				response = Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}" + Constant.SERVER_ERROR;
			}
			/* before 1 month ago news method called */
			beforeOneMonthNewsDeleted();
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
			response = "Exception! status - {}" + e.getMessage();
		}
		return response;
	}

	public void saudiNews(Integer currentPage) {
		Integer lenght, count = 0;
		try {
			String strResponse1 = thirdPartyApiUtil.getAllSaudiNews("ksa", currentPage);
			ObjectMapper mapper1 = new ObjectMapper();
			mapper1.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (!strResponse1.isBlank()) {
				Map<?, ?> mapResponse1 = mapper1.readValue(strResponse1, Map.class);
				SaudiNewsResponse ksanews1 = mapper1.convertValue(mapResponse1, SaudiNewsResponse.class);
				if (ksanews1.getStatusCode().equals("200") && ksanews1.getMessageCode().equals("success")) {
					SaRobotsItems robotsItems = new SaRobotsItems();
					log.info("find the lenght inside the robot in task size:- "
							+ ksanews1.getResult().getRobotTasks().getItems().size());
					lenght = ksanews1.getResult().getRobotTasks().getItems().size() - 1;
					log.info("get the items task lenght :- " + lenght);
					BeanUtils.copyProperties(ksanews1.getResult().getRobotTasks().getItems().get(lenght), robotsItems);
					log.info("Data copy third party to local class :- " + robotsItems);
					/* Saudi news updated */
					for (SaudiMarketNews items : robotsItems.getCapturedLists().getSaudinewsenglish()) {
						if (!items.get_STATUS().equals("REMOVED")) {
							GlobalNews oldnews = newsRepository.findByTitleAndPublishedAndTypeAndSubType(
									items.getTitle(), DateUtil.changeStringaDateFormat(items.getDate()), "KSA", "news");
							if (oldnews == null) {
								count++;
								GlobalNews globalNews = new GlobalNews();
								globalNews.setTitle(items.getTitle());
								globalNews.setDescription(items.getDescription());
								globalNews.setLink(items.getLink());
								globalNews.setPublished(DateUtil
										.changeStringaDateFormat(items.getDate() != null ? items.getDate() : "0"));
								globalNews.setImage_url(items.getImage());
								globalNews.setSource("By Saudiexchange.sa");
								globalNews.setType("KSA");
								globalNews.setSubType("news");
								globalNews.setCreationDate(new Date());
								newsRepository.save(globalNews);
								log.info(count + " saudi news save successfully" + "! status - {}", Constant.OK);
							} else {
								oldnews.setUpdationDate(new Date());
								newsRepository.save(oldnews);
								log.info("this saudi news alreay found in our database can't saved");
							}
						}
					}
				} else {
					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
	}

	public void usaNews(Integer usaCurrentPage) {
		Integer lenght, usacount = 0;
		try {
			String strResponse2 = thirdPartyApiUtil.getAllUsaNews("usa", usaCurrentPage);
			ObjectMapper mapper2 = new ObjectMapper();
			mapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (!strResponse2.isBlank()) {
				Map<?, ?> mapResponse1 = mapper2.readValue(strResponse2, Map.class);
				UsNewsResponse usanews1 = mapper2.convertValue(mapResponse1, UsNewsResponse.class);
				if (usanews1.getStatusCode().equals("200") && usanews1.getMessageCode().equals("success")) {
					UsRobotsItems robotsItems = new UsRobotsItems();
					log.info("find the lenght inside the robot in task size:- "
							+ usanews1.getResult().getRobotTasks().getItems().size());
					lenght = usanews1.getResult().getRobotTasks().getItems().size() - 1;
					log.info("get the items task lenght :- " + lenght);
					BeanUtils.copyProperties(usanews1.getResult().getRobotTasks().getItems().get(lenght), robotsItems);
					log.info("Data copy third party to local class :- " + robotsItems);
					for (SaudiMarketNews items : robotsItems.getCapturedLists().getUsnewsenglish()) {
						/* null object are not added in our database */

						if (items.get_STATUS() != null) {
							if (!items.get_STATUS().equals("REMOVED")) {
								GlobalNews news = newsRepository.findByTitleAndTypeAndSubType(items.getTitle(), "USA",
										"news");
								if (news == null) {
									usacount++;
									GlobalNews globalNews = new GlobalNews();
									globalNews.setTitle(items.getTitle());
									globalNews.setDescription(items.getDescription());
									globalNews.setLink(items.getLink());
//									globalNews.setPublished(DateUtil.MinToStringDateTimeFormat(
//											items.getDate() != null ? items.getDate() : "0"));
									
									globalNews.setPublished(DateUtil  
											.convertRelativeTime(items.getDate() != null ? items.getDate() : "0"));
									
									if (items.getProvider() != null && !items.getProvider().isBlank()) {
										if (items.getProvider().contains("By")) {
											globalNews.setSource(items.getProvider());
										} else {
											globalNews.setSource(Constant.BY + " " + items.getProvider());
										}
									}
//									globalNews.setSource(items.getProvider());
									globalNews.setImage_url(items.getImage());
									globalNews.setType("USA");
									globalNews.setSubType("news");
									globalNews.setCreationDate(new Date());
									newsRepository.save(globalNews);
									log.info(usacount + " USA news save successfully" + "! status - {}", Constant.OK);
								} else {
									news.setUpdationDate(new Date());
									newsRepository.save(news);
									log.info("this USA news alreay found in our database can't saved");
								}
							}
						} else {
							GlobalNews news = newsRepository.findByTitleAndTypeAndSubType(items.getTitle(), "USA",
									"news");
							if (news == null) {
								usacount++;
								GlobalNews globalNews = new GlobalNews();
								globalNews.setTitle(items.getTitle());
								globalNews.setDescription(items.getDescription());
								globalNews.setLink(items.getLink());
//								globalNews.setPublished(DateUtil
//										.MinToStringDateTimeFormat(items.getDate() != null ? items.getDate() : "0"));
								
								globalNews.setPublished(DateUtil  
										.convertRelativeTime(items.getDate() != null ? items.getDate() : "0"));
								
								if (items.getProvider() != null && !items.getProvider().isBlank()) {
									if (items.getProvider().contains("By")) {
										globalNews.setSource(items.getProvider());
									} else {
										globalNews.setSource(Constant.BY + " " + items.getProvider());
									}
								}
//								globalNews.setSource(items.getProvider());
								globalNews.setImage_url(items.getImage());
								globalNews.setType("USA");
								globalNews.setSubType("news");
								globalNews.setCreationDate(new Date());
								newsRepository.save(globalNews);
								log.info(usacount + " USA news save successfully" + "! status - {}", Constant.OK);
							} else {
								news.setUpdationDate(new Date());
								newsRepository.save(news);
								log.info("this USA news alreay found in our database can't saved");
							}

						}
					}
				} else {
					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
	}

	public void crytpoNews(Integer cryptoCurrentPage) {
		Integer lenght, cryptocount = 0;
		try {
			String cryptoResponse1 = thirdPartyApiUtil.getCryptoNews(cryptoCurrentPage);
			ObjectMapper mapper4 = new ObjectMapper();
			mapper4.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (!cryptoResponse1.isBlank()) {
				Map<?, ?> mapResponse1 = mapper4.readValue(cryptoResponse1, Map.class);
				CryptoNewsResponse cryptonews1 = mapper4.convertValue(mapResponse1, CryptoNewsResponse.class);
				if (cryptonews1.getStatusCode().equals("200") && cryptonews1.getMessageCode().equals("success")) {
					RobotsItems robotsItems = new RobotsItems();
					log.info("find the lenght inside the robot in task size:- "
							+ cryptonews1.getResult().getRobotTasks().getItems().size());
					lenght = cryptonews1.getResult().getRobotTasks().getItems().size() - 1;
					log.info("get the items task lenght :- " + lenght);
					BeanUtils.copyProperties(cryptonews1.getResult().getRobotTasks().getItems().get(lenght),
							robotsItems);
					log.info("Data copy third party to local class :- " + robotsItems);
					for (SaudiMarketNews items : robotsItems.getCapturedLists().getCryptonews()) {
						if(items.get_STATUS()!=null) {
							if (!items.get_STATUS().equals("REMOVED")) {
								GlobalNews globalNews1 = newsRepository.findByTitleAndTypeAndSubType(items.getTitle(),
										"Crypto", "news");
								if (globalNews1 == null) {
									cryptocount++;
									GlobalNews globalNews = new GlobalNews();
									globalNews.setTitle(items.getTitle());
									globalNews.setDescription(items.getDescription());
									globalNews.setLink(items.getLink());
//									globalNews.setPublished(DateUtil
//											.MinToStringDateTimeFormat(items.getDate() != null ? items.getDate() : "0"));
									globalNews.setPublished(DateUtil  
											.convertRelativeTime(items.getDate() != null ? items.getDate() : "0"));
									
									if (items.getProvider() != null && !items.getProvider().isBlank()) {
										if (items.getProvider().contains("By")) {
											globalNews.setSource(items.getProvider());
										} else {
											globalNews.setSource(Constant.BY + " " + items.getProvider());
										}
									}
//									globalNews.setSource(items.getProvider());
									globalNews.setImage_url(items.getImage());
									globalNews.setType("Crypto");
									globalNews.setSubType("news");
									globalNews.setCreationDate(new Date());
									newsRepository.save(globalNews);
									log.info(cryptocount + " Crypto news save successfully" + "! status - {}", Constant.OK);
								} else {
									globalNews1.setUpdationDate(new Date());
									newsRepository.save(globalNews1);
									log.info("this Crypto news alreay found in our database can't saved");
								}
							}
						}else {

							GlobalNews globalNews1 = newsRepository.findByTitleAndTypeAndSubType(items.getTitle(),
									"Crypto", "news");
							if (globalNews1 == null) {
								cryptocount++;
								GlobalNews globalNews = new GlobalNews();
								globalNews.setTitle(items.getTitle());
								globalNews.setDescription(items.getDescription());
								globalNews.setLink(items.getLink());
//								globalNews.setPublished(DateUtil  
//										.MinToStringDateTimeFormat(items.getDate() != null ? items.getDate() : "0"));
								
								globalNews.setPublished(DateUtil  
										.convertRelativeTime(items.getDate() != null ? items.getDate() : "0"));
								
								if (items.getProvider() != null && !items.getProvider().isBlank()) {
									if (items.getProvider().contains("By")) {
										globalNews.setSource(items.getProvider());
									} else {
										globalNews.setSource(Constant.BY + " " + items.getProvider());
									}
								}
//								globalNews.setSource(items.getProvider());
								globalNews.setImage_url(items.getImage());
								globalNews.setType("Crypto");
								globalNews.setSubType("news");
								globalNews.setCreationDate(new Date());
								newsRepository.save(globalNews);
								log.info(cryptocount + " Crypto news save successfully" + "! status - {}", Constant.OK);
							} else {
								globalNews1.setUpdationDate(new Date());
								newsRepository.save(globalNews1);
								log.info("this Crypto news alreay found in our database can't saved");
							}
						
						}
						
					}
				} else {
					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
	}

	public void announcementNews(Integer announcementCurrentPage) {
		Integer lenght, newscount = 0;
		try {
			String announcementnewsresponse1 = thirdPartyApiUtil.getAllAnnouncementNews(announcementCurrentPage);
			ObjectMapper announcementmapper1 = new ObjectMapper();
			announcementmapper1.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (!announcementnewsresponse1.isBlank()) {
				Map<?, ?> mapResponse1 = announcementmapper1.readValue(announcementnewsresponse1, Map.class);
				AnnouncementNewsResponse announcementnews1 = announcementmapper1.convertValue(mapResponse1,
						AnnouncementNewsResponse.class);
				if (announcementnews1.getStatusCode().equals("200")
						&& announcementnews1.getMessageCode().equals("success")) {
					AnnouncementRobotsItems robotsItems = new AnnouncementRobotsItems();
					log.info("find the lenght inside the robot in task size:- "
							+ announcementnews1.getResult().getRobotTasks().getItems().size());
					lenght = announcementnews1.getResult().getRobotTasks().getItems().size() - 1;
					log.info("get the items task lenght :- " + lenght);
					BeanUtils.copyProperties(announcementnews1.getResult().getRobotTasks().getItems().get(lenght),
							robotsItems);
					log.info("Data copy third party to local class :- " + robotsItems);
					for (SaudiMarketNews items : robotsItems.getCapturedLists().getAnnouncementnews()) {
						if (!items.get_STATUS().equals("REMOVED")) {
							int length;
							length = items.getDate().length();
							String datetime = items.getDate().substring(length - 19);
							GlobalNews globalNews1 = newsRepository.findByTitleAndPublishedAndTypeAndSubType(
									items.getTitle(), DateUtil.changeStringaDateFormat(datetime), "KSA",
									"AnnouncementNews");
							if (globalNews1 == null) {
								GlobalNews globalNews = new GlobalNews();
								newscount++;
								globalNews.setTitle(items.getTitle());
								globalNews.setDescription(items.getDescription());
								globalNews.setLink(items.getLink());
								globalNews.setPublished(
										DateUtil.changeStringaDateFormat(datetime != null ? datetime : "0"));
								globalNews.setSource("By Saudiexchange.sa");
								globalNews.setPercentage(items.getPercentage());
								globalNews.setType("KSA");
								globalNews.setSymbol(items.getSymbol());
								globalNews.setSubType("AnnouncementNews");
								globalNews.setCreationDate(new Date());
								Stock stock = stockRepository.findBySymbol(items.getSymbol());
								/* set announcement news logo */
								if (stock != null) {
									globalNews.setImage_url(stock.getStockProfile().getLogo());
									log.info(newscount + " Announcement news updated successfully" + "! status - {}",
											Constant.OK);
								} else {
									log.info(items.getSymbol()
											+ " this stock are not found in our database. you can't save logo !!");
								}
								newsRepository.save(globalNews);
								log.info(newscount + " Announcement news updated successfully" + "! status - {}",
										Constant.OK);
							} else {
								Stock stock = stockRepository.findBySymbol(globalNews1.getSymbol());
								/* set announcement news logo */
								if (stock != null) {
									log.info("this Announcement news alreay found in our database can't saved");
									globalNews1.setImage_url(stock.getStockProfile().getLogo());
									log.info(globalNews1.getSymbol() + " this news logo saved succesfully !!");
									log.info(newscount + " Announcement news updated successfully" + "! status - {}",
											Constant.OK);
								} else {
									log.info(globalNews1.getSymbol()
											+ " this stock are not found in our database. you can't save logo !!");
								}
								globalNews1.setUpdationDate(new Date());
								newsRepository.save(globalNews1);
							}
						}
					}
				} else {
					log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
				}
			} else {
				log.info(Constant.INTERNAL_SERVER_ERROR_MESSAGE + "! status - {}", Constant.SERVER_ERROR);
			}
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
	}

	public void beforeOneMonthNewsDeleted() {
		try {
			/* deleted before 1 month announcement news from our database */
			System.out.println(DateUtil.currentDateTimeToOneMonth());
			List<GlobalNews> oldsaudinews = newsRepository.findByPublishedBefore(DateUtil.currentToOneMonth());
			if (oldsaudinews != null && !oldsaudinews.isEmpty()) {
				newsRepository.deleteAll(oldsaudinews);
				log.info("Before 1 month all Old" + oldsaudinews.size() + " news deleted succesfully ! status - {}",
						Constant.OK);
			} else {
				log.info("Befor 1 month old " + oldsaudinews.size() + " news not found ! status - {}", Constant.OK);
			}
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
	}

	/* stock news saved from (financialmodelingprep.com) FMPP */
	public class NewsFormFmpTask implements Runnable {
		private List<Country> list;

		public NewsFormFmpTask(List<Country> list) {
			super();
			this.list = list;
		}

		@Override
		public void run() {
			SaveStockNewsFormFmp(list);
		}
	}

	public void newsFormFmpThread(List<Country> countryList) {
		NewsFormFmpTask newsFormFmpTask = new NewsFormFmpTask(countryList);
		Thread thread = new Thread(newsFormFmpTask);
		thread.start();
	}

	public void SaveStockNewsFormFmp(List<Country> countryList) {
//		String us_country = "united states";
		try {
			for (Country country : countryList) {
				if (country != null) {
					/* get USA stock in our local database */
					Integer newDataCount = 0, notFoundCount = 0;
					List<Stock> usa_stock_list = stockRepository.getUsaStockList(country.getCountry());
					log.info(country.getCountry().toUpperCase() + " stock list size " + usa_stock_list.size());
					if (usa_stock_list != null && !usa_stock_list.isEmpty()) {
						for (Stock stock : usa_stock_list) {
							String stockResponse = thirdPartyApiUtil.get_usa_stock_news(stock.getSymbol());
//					log.info("third party response " + stockResponse);
							if (!stockResponse.isBlank() && !stockResponse.contains("[]")) {
								Type collectionType = new TypeToken<List<StockNews>>() {
								}.getType();
								List<StockNews> stockNewsList = new Gson().fromJson(stockResponse, collectionType);
								if (!stockNewsList.isEmpty()) {
									for (StockNews stocknews : stockNewsList) {
										/* deleted before 1 month announcement news from our database */
//								System.out.println(DateUtil.currentDateTimeToOneMonth());
										String beforeOneMonthDate = DateUtil.currentDateTimeToOneMonth();
										String stockNewPublishedDate = stocknews.getPublishedDate();
										// Define date-time formatter to parse strings
										DateTimeFormatter formatter = DateTimeFormatter
												.ofPattern("yyyy-MM-dd HH:mm:ss");

										// Parse strings to LocalDateTime objects
										LocalDateTime dateTime1 = LocalDateTime.parse(beforeOneMonthDate, formatter);
										LocalDateTime dateTime2 = LocalDateTime.parse(stockNewPublishedDate, formatter);
										if (stocknews.getPublishedDate().equals(DateUtil.currentDateTimeToOneMonth())
												|| dateTime2.isAfter(dateTime1)) {
											GlobalNews oldglobalNews = newsRepository
													.findByTitleAndPublishedAndTypeAndSubType(stocknews.getTitle(),
															stocknews.getPublishedDate(), "USA", "Stock");
											if (oldglobalNews == null) {
												GlobalNews newglobalNews = new GlobalNews();
												newglobalNews.setSymbol(stocknews.getSymbol());
												newglobalNews.setPublished(stocknews.getPublishedDate());
												newglobalNews.setTitle(stocknews.getTitle());
												newglobalNews.setImage_url(stocknews.getImage());
												if (stocknews.getSite() != null && !stocknews.getSite().isBlank()) {
													if (stocknews.getSite().contains("By")) {
														newglobalNews.setSource(stocknews.getSite());
													} else {
														newglobalNews
																.setSource(Constant.BY + " " + stocknews.getSite());
													}
												}
//												newglobalNews.setSource(stocknews.getSite());
												newglobalNews.setDescription(stocknews.getText());
												newglobalNews.setLink(stocknews.getUrl());
												newglobalNews.setSubType("Stock");
												newglobalNews.setType("USA");
												newglobalNews.setUpdationDate(new Date());
												newsRepository.save(newglobalNews);
												newDataCount++;
												log.info(newDataCount + " : " + stocknews.getSymbol()
														+ " - USA stock news saved successfully !! | status - {}",
														Constant.OK);
											}
//									else {
//										oldglobalNews.setUpdationDate(new Date());
//										newsRepository.save(oldglobalNews);
//										oldDataCount++;
//										log.info(oldDataCount + " : " + stocknews.getSymbol()
//												+ " - This news already available into the database ! status - {}",
//												Constant.OK);
//									}
										} else {
											log.info(" Before 1 month news :-" + stocknews.getPublishedDate());
										}
									}
									log.info("this stock name :- " + stock.getName()
											+ "is All USA stock news updated successfully !! - status {} "
											+ Constant.OK);
								} else {
									notFoundCount++;
									log.info(notFoundCount + " : " + stock.getSymbol() + " - Symbol "
											+ Constant.DATA_NOT_FOUND_FROM_THIRD_PARTY_MESSAGE + "! status - {}",
											Constant.SERVER_ERROR);
								}
							} else {
								notFoundCount++;
								log.info(notFoundCount + " : " + stock.getSymbol() + " - symbol not found "
										+ "! status - {}", Constant.NOT_FOUND);
							}
						}
					} else {
						log.info("USA stocks list not found ! status - {} ", Constant.OK);
					}
					/* before 1 month ago news method called */
					beforeOneMonthNewsDeleted();
				} else {
					log.info("Country not found : status - {}", Constant.NOT_FOUND);
				}
			}
		} catch (Exception e) {
			log.info("Exception! status - {}", e.getMessage());
		}
	}
}
