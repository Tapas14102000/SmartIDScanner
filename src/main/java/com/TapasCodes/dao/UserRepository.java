package com.TapasCodes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.TapasCodes.entities.user;

public interface UserRepository extends JpaRepository<user, Integer> {
	@Query("from user u where u.email = :email")
	public user getUserByUserName(@Param("email") String email);
}
