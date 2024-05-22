package com.advantal.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.FavoriteStock;

@Repository
public interface FavoriteStockRepository extends JpaRepository<FavoriteStock, Long> {

	List<FavoriteStock> findByUserId(Long userId);

//	FavoriteStock findByIdAndStatus(Long id, Short one);
//
//	FavoriteStock findByStockId(Long stockId);
//
//	Page<FavoriteStock> findByUserId(Long userId, Pageable pageable);
//
//	Page<FavoriteStock> findByUserIdAndStatus(Long userId, Short one, Pageable pageable);
//
	Page<FavoriteStock> findByUserIdAndStatus(Long userId, Short one, Pageable pageable);

//
	Page<FavoriteStock> findByUserIdAndCountryAndStatus(Long userId, String country, Short one, Pageable pageable);
//
//	@Query(value ="SELECT * FROM favorite_stock fav WHERE fav.status!=0 and fav.stock_id_fk=? and fav.user_id=?",nativeQuery = true)
//	FavoriteStock findFavoriteStock(Long stockId, Long userId);
//
//	FavoriteStock findByIdAndUserId(Long id, Long userId);

	@Query(value = "SELECT * FROM favorite_stock fav WHERE fav.user_id=?1 and fav.country=?2 and fav.instrument_type=?3 and (fav.name like concat('%',?4,'%') or fav.symbol like concat('%',?4,'%') or fav.exchange like concat('%',?4,'%') or fav.country like concat('%',?4,'%'))", countQuery = "SELECT count(*) FROM favorite_stock fav WHERE fav.user_d=?1 and fav.country=?2 and fav.instrument_type=?3 and (fav.name like concat('%',?4,'%') or fav.symbol like concat('%',?4,'%') or fav.exchange like concat('%',?4,'%') or fav.country like concat('%',?4,'%'))", nativeQuery = true)
	Page<FavoriteStock> findAllFavoriteStocks(Long userId, String country, String instrument_type, String keyWord,
			Pageable pageable);

}
