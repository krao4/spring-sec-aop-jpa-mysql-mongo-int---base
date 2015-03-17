package com.sapient.springapp.service;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService
{
	
    private static final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
    private static final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");
	
	public UserDetails loadUserByUsername(String username)
	        throws UsernameNotFoundException, DataAccessException
    {
		System.out.println("username recieved :: " + username);
		
        Collection<? extends GrantedAuthority> authorities = createAuthorities(username);
		
		UserDetails user = new User(username, "password", true, true, true, true, authorities);
		return user;
    }
	
    public static Collection<? extends GrantedAuthority> createAuthorities(String username) {

    	//Add your custom authorization business logic here;
    	//The below code is only for demonstration purposes.
    	if(username.equalsIgnoreCase("user1")) {
    		return USER_ROLES;
    	} else if(username.equalsIgnoreCase("admin1")) { 
    		return ADMIN_ROLES;
    	} else {
    		return null;
    	}
        
    }	
}
