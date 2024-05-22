package com.advantal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.GlobalNews;

@Repository
public interface NewsRepository extends JpaRepository<GlobalNews, Long> {

//	@Query(value = "SELECT * FROM crypto us WHERE us.status!=0 and us.listing_time!=0 and us.listing_time>= :oneYearOldDate ORDER BY us.listing_time DESC", countQuery = "SELECT count(*) from crypto us WHERE us.status != 0 and us.listing_time!=0 and listing_time>= :oneYearOldDate ORDER BY us.listing_time DESC", nativeQuery = true)
	List<GlobalNews> findByTypeAndSubType(String type, String sub_type);

	//ORDER BY st.market_cap DESC
	@Query(value = "SELECT * FROM global_news news WHERE (news.type=? and news.sub_type=?) ORDER BY news.published DESC", countQuery = "SELECT count(*) from global_news news WHERE (news.type=? and news.sub_type=?) ORDER BY news.published DESC", nativeQuery = true)
	Page<GlobalNews> findAllNews(String type, String sub_type, Pageable pageable);

//	@Query(value = "SELECT * FROM global_news news WHERE news.type=?", countQuery = "SELECT count(*) from global_news news WHERE news.type=?", nativeQuery = true)
//	Page<GlobalNews> findAllNews(String type, Pageable pageable);

	@Query(value = "SELECT * FROM global_news news WHERE (news.type=?) and (news.source like concat('%',?,'%') or news.title like concat('%',?,'%') or news.description like concat('%',?,'%')) ORDER BY news.published DESC", countQuery = "SELECT * FROM global_news news WHERE news.type=? and (news.source like concat('%',?,'%') or news.title like concat('%',?,'%') or news.description like concat('%',?,'%')) ORDER BY news.published DESC", nativeQuery = true)
	Page<GlobalNews> findAllSearchingNews(String type, String keyword,String keyword1, String keyword2, Pageable pageable);

//	@Query(value = "SELECT * FROM global_news news WHERE news.type=? and news.sub_type=? and (news.source like concat('%',?,'%') or news.title like concat('%',?,'%'))",countQuery = "SELECT * FROM global_news news WHERE news.type=? and news.sub_type=? and (news.source like concat('%',?,'%') or news.title like concat('%',?,'%'))", nativeQuery = true)
//	Page<GlobalNews> findAllSaudiAndAnnouncementSearchingNews(String type, String sub_type,String keyword1, String keyword,Pageable pageable);

//	@Query(value = "SELECT * FROM global_news WHERE published_at<= :resultDate", countQuery = "SELECT count(*) from global_news WHERE published_at=?", nativeQuery = true)
//	List<GlobalNews> findPublishedAt(String resultDate);

	GlobalNews findByTitleAndPublishedAndTypeAndSubType(String title, String date, String string, String string2);

//	@Query(value = "SELECT * FROM global_news us WHERE us.listing_time>= :oneYearOldDate", countQuery = "SELECT count(*) from crypto us WHERE listing_time>= :oneYearOldDate", nativeQuery = true)
//	List<GlobalNews> findByPublished_at(String published_at);

	List<GlobalNews> findByPublishedBefore(String string);

	GlobalNews findByTitleAndPublished(String title, String date);

	GlobalNews findByPublished(String date);

	List<GlobalNews> findByTypeAndSubTypeAndPublishedBefore(String type, String sub_type, String currentToOneMonth);

	GlobalNews findByTitleAndTypeAndSubType(String title, String string, String string2);

	@Query(value = "SELECT * FROM global_news news WHERE (news.type=? and news.symbol=?) ORDER BY news.published DESC", countQuery = "SELECT count(*) from global_news news WHERE (news.type=? and news.symbol=?) ORDER BY news.published DESC", nativeQuery = true)
	Page<GlobalNews> findAllStockNews(String country, String symbol,Pageable pageable);

}
