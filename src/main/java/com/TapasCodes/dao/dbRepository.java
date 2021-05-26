package com.TapasCodes.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.TapasCodes.entities.user;
import com.TapasCodes.entities.userdb;

public interface dbRepository extends JpaRepository<userdb, Integer>{

	@Query("from userdb as c where c.user.id =:userId order by c.dbid desc")
	public Page<userdb> findHistoryByUser(@Param("userId") int userId, Pageable pageable);
	
	@Query("from userdb as c where c.upload =:text and c.user =:user")
	public userdb findimg(@Param("text") String text,@Param("user") user User);

	@Query("select count(*) from userdb as c where c.user =:user")
	public int countbyid(@Param("user") user User);
}
