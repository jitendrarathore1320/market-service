//package com.advantal.repository;
//
//import java.util.List;
//
//import javax.validation.constraints.NotNull;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.advantal.model.FavoriteInstrument;
//import com.advantal.model.User;
//
//@Repository
//public interface FavoriteInstrumentRepository extends JpaRepository<FavoriteInstrument, Long> {
//
////	FavoriteInstrument findByUserIdAndInstrumentIdAndSymbol(Long userId, Long instrumentId, String symbol);
//
//	Page<FavoriteInstrument> findByUserId(Long userId, Pageable pageable);
//
////	Page<FavoriteInstrument> findByUserIdAndCountry(Long userId, String country, Pageable pageable);
//
//	Page<FavoriteInstrument> findByUserIdAndInstrumentType(Long userId, String instrument_type, Pageable pageable);
//
////	FavoriteInstrument findByUserIdAndInstrumentIdAndCryptoId(Long userId, Long instrumentId, String cryptoId);
//
////	FavoriteInstrument findByUserIdAndInstrumentId(Long userId, Long instrumentId);
//
//	@Query(value = "SELECT * FROM favorite_instrument fav WHERE fav.user_id_fk = (SELECT u.id FROM user u WHERE u.id = ?1)", nativeQuery = true)
////	@Query(value = "SELECT * FROM favorite_instrument fav WHERE fav.user_id_fk = (SELECT u.id FROM user u WHERE u.id = ?1)", nativeQuery = true)
//	List<FavoriteInstrument> findFavorite(Long userId);
////	FavoriteInstrument findFavorite(Long userId);
//
//}
