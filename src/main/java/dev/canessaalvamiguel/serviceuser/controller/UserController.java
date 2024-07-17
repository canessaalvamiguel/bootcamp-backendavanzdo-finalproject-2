package dev.canessaalvamiguel.serviceuser.controller;

import dev.canessaalvamiguel.serviceuser.entities.User;
import dev.canessaalvamiguel.serviceuser.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@AllArgsConstructor
public class UserController {

  UserService userService;

  @GetMapping
  public ResponseEntity<List<User>> getUsers(){
    log.info("Getting all users");
    return ResponseEntity.ok(userService.getUsers());
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> getUserById(@PathVariable Long userId){
    log.info("Getting user by id: {}", userId);
    return ResponseEntity.ok(userService.getUserById(userId));
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user){
    log.info("Creating new user: {}", user);
    return ResponseEntity.ok(userService.createUser(user));
  }

}
