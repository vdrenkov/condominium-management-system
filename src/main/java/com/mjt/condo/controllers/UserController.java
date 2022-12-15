package com.mjt.condo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.mjt.condo.exceptions.UserAlreadyExistsException;
import com.mjt.condo.exceptions.UserNotAuthenticatedException;
import com.mjt.condo.exceptions.UserNotFoundException;
import com.mjt.condo.models.User;
import com.mjt.condo.services.UserService;

import lombok.SneakyThrows;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @SneakyThrows
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
    
    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) throws UserAlreadyExistsException, UserNotFoundException {
        User newAccount = userService.createUser(user);
        return new ResponseEntity<User>(newAccount, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user)
    		throws AccessDeniedException, UserNotFoundException, UserNotAuthenticatedException {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }    

    @ExceptionHandler(value = UserNotFoundException.class)
    private ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    private ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(value = UserNotAuthenticatedException.class)
    private ResponseEntity<String> handleUserNotAuthenticatedException(UserNotAuthenticatedException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    private ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }
}
