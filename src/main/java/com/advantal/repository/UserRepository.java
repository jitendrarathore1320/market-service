package com.advantal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByIdAndStatus(Long userId, Short status);
	
	
//	select User from user u where u.userId=   select user_id from favourite where f.id = 1;


}
