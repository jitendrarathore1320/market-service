package com.advantal.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advantal.requestpayload.NewsRequestPayload;
import com.advantal.requestpayload.StockNewsRequestPayload;
import com.advantal.service.NewsService;
import com.advantal.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/global_news")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NewsController {
	@Autowired
	NewsService newsService;

	/* get all news and based on country */
	@PostMapping("/get_all_news")
	ResponseEntity<Map<String, Object>> get_all_news(@RequestBody @Valid NewsRequestPayload newsRequestPayloadS) {
		log.info("inside get_all_news  status - {}", Constant.OK);
		return new ResponseEntity<Map<String, Object>>(newsService.getAllNews(newsRequestPayloadS), HttpStatus.OK);
	}

	/* get all news and based on country */
	@PostMapping("/get_stock_news")
	ResponseEntity<Map<String, Object>> get_stock_news(
			@RequestBody @Valid StockNewsRequestPayload stockNewsRequestPayload) {
		log.info("inside get_all_news  status - {}", Constant.OK);
		return new ResponseEntity<Map<String, Object>>(newsService.getStockNews(stockNewsRequestPayload),
				HttpStatus.OK);
	}

}
