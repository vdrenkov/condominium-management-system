package com.mjt.condo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mjt.condo.models.User;
import com.mjt.condo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepo.findByUsername(username);
		if (userOptional.isEmpty()) {
			throw new UsernameNotFoundException("Username not found.");
		}
		User user = userOptional.get();
		List<GrantedAuthority> grantedPrivileges = user
				.getPrivileges()
				.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getName()))
				.collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedPrivileges);
	}
}