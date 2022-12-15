package com.mjt.condo.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mjt.condo.models.Privilege;
import com.mjt.condo.models.User;
import com.mjt.condo.repositories.PrivilegeRepository;
import com.mjt.condo.repositories.UserRepository;

import com.mjt.condo.exceptions.UserAlreadyExistsException;
import com.mjt.condo.exceptions.UserNotAuthenticatedException;
import com.mjt.condo.exceptions.UserNotFoundException;

@Service
public class UserService {

	@Autowired
	PrivilegeRepository privilegeRepository;

	@Autowired
	private UserRepository userRepository;

	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public User createUser(User user) throws UserAlreadyExistsException, UserNotFoundException {

		boolean isExistingUser = userRepository.findByUsername(user.getUsername()).isPresent();

		if (isExistingUser) {
			throw new UserAlreadyExistsException("User already exists.");
		}

		user.setPrivileges(setPrivilegesByUser(user));
		user.setPassword(passwordEncoder().encode(user.getPassword()));
		User createdUser = userRepository.save(user);

		return createdUser;
	}

	public User updateUser(Long id, User user)
			throws UserNotFoundException, UserNotAuthenticatedException, AccessDeniedException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User loggedUser = userRepository.findByUsername(auth.getName())
				.orElseThrow(() -> new UserNotAuthenticatedException("User not authenticated."));

		User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));

		existingUser.setUsername(user.getUsername());
		existingUser.setPassword(user.getPassword());
		existingUser.setPrivileges(user.getPrivileges());

		String loggedUserName = loggedUser.getUsername();

		Boolean isOwner = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ROLE_ADMIN));

		if (!loggedUserName.equals(existingUser.getUsername()) && !isOwner) {
			throw new AccessDeniedException("Access denied.");
		}

		if (user.getPassword() != null) {
			existingUser.setPassword(passwordEncoder().encode(user.getPassword()));
		}

		if (existingUser.getPrivileges().isEmpty()) {
			existingUser.setPrivileges(setPrivilegesByUser(existingUser));
		}

		User createdUser = userRepository.save(existingUser);

		return createdUser;
	}

	public void deleteById(Long id) throws UserNotFoundException {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new UserNotFoundException("User not found.");
		}
	}

	public List<User>findAllUsers()
	{
		return userRepository.findAll();
	}
	
	public User findById(Long id) throws UserNotFoundException {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
		return user;
	}

	public User findByUsername(String username) throws UserNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(String.format("User not found.")));
		return user;
	}

	public User authenticateByUsername(String username) throws UserNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found."));
		return user;
	}

	public List<GrantedAuthority> getGrantedPrivilegesByName(String username) throws UserNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        var privileges = user.getPrivileges();
        if (privileges == null || privileges.isEmpty()) {
            return Collections.emptyList();
        }

        return privileges
                .stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
                .collect(Collectors.toList());
    }

	public Set<Privilege> setPrivilegesByUser(User user) throws UserNotFoundException {
		Set<Privilege> privileges = user.getPrivileges();
		if (privileges == null || privileges.isEmpty()) {
			privileges = new HashSet<>();
			privilegeRepository.findById(ROLE_USER).ifPresent(privileges::add);
		}

		return privileges;
	}
}
