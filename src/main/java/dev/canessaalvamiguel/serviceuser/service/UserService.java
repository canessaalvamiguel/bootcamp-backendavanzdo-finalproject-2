package dev.canessaalvamiguel.serviceuser.service;

import dev.canessaalvamiguel.serviceuser.entities.User;
import dev.canessaalvamiguel.serviceuser.exceptions.EmailExistsException;
import dev.canessaalvamiguel.serviceuser.repository.UserRepository;
import dev.canessaalvamiguel.serviceuser.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

  UserRepository userRepository;

  public List<User> getUsers(){
    return userRepository.findAll();
  }

  public User getUserById(Long userId){
    return userRepository.findById(userId)
        .orElseThrow(
            () -> new NotFoundException("User with id " + userId + " not found.")
        );
  }

  public User createUser(User user){
    if (userRepository.findByEmail(user.getEmail()) != null) {
      throw new EmailExistsException("Email already exists");
    }
    return userRepository.save(user);
  }
}
