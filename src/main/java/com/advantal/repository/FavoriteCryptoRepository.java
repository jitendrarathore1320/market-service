package com.advantal.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.model.FavoriteCrypto;

@Repository
public interface FavoriteCryptoRepository extends JpaRepository<FavoriteCrypto, Long> {

	List<FavoriteCrypto> findByUserId(Long userId);

	Page<FavoriteCrypto> findByUserIdAndStatus(Long userId, Short one, Pageable pageable);

	@Query(value = "SELECT * FROM favorite_crypto fav WHERE fav.user_id=?1 and fav.instrument_type=?2 and (fav.name like concat('%',?3,'%') or fav.symbol like concat('%',?3,'%'))", countQuery = "SELECT count(*) FROM favorite_crypto fav WHERE fav.user_id=?1 and fav.instrument_type=?2 and (fav.name like concat('%',?3,'%') or fav.symbol like concat('%',?3,'%'))", nativeQuery = true)
	Page<FavoriteCrypto> findAllFavoriteCrypto(Long userId, String instrument_type, String keyWord, Pageable pageable);

//	FavoriteCrypto findByUserIdAndStatus(Long userId, Short one);

//	@Query(value ="SELECT * FROM favorite_crypto fav WHERE fav.status!=0 and fav.crypto_id_fk=? and fav.user_id=?",nativeQuery = true)
//	FavoriteCrypto findFavoriteCrypto(Long cryptoId, Long userId);

//	@Query(value = "SELECT * FROM favorite_instrument fav WHERE fav.user_id_fk = (SELECT u.id FROM user u WHERE u.id = ?1)", nativeQuery = true)
//	FavoriteCrypto findFavoriteCrypto(Long cryptoId, Long userId);
//
//	FavoriteCrypto findByIdAndUserId(Long id, Long userId);

}
