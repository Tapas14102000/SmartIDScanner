package com.TapasCodes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.TapasCodes.dao.UserRepository;
import com.TapasCodes.entities.user;

public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	private UserRepository us;
	public UserDetails loadUserByUsername(String Regd) throws UsernameNotFoundException {
		user st=us.getUserByUserName(Regd);
		if(st==null)
			throw new UsernameNotFoundException("Couldn't found user!");
		CustomUserDetails cust=new CustomUserDetails(st);
		return cust;
	}

}
