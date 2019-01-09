package no.ntnu.sysdev.SpringBoot_Demo.service;

import no.ntnu.sysdev.SpringBoot_Demo.entity.User;
import no.ntnu.sysdev.SpringBoot_Demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<String> createUser(User user) {
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (null == foundUser) {
            userRepository.save(user);
            return new ResponseEntity<>("User created", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> deleteUser(String email) {
        User userToDelete = userRepository.findByEmail(email);
        if (null != userToDelete) {
            userRepository.delete(userToDelete);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
    }

}
