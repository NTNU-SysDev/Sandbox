package no.ntnu.sysdev.SpringBoot_Demo.service;

import no.ntnu.sysdev.SpringBoot_Demo.entity.User;
import no.ntnu.sysdev.SpringBoot_Demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is annotated with @Service which
 * means it holds the business logic and call method
 * in repository layer. In this case, this service
 * holds our business logic attached with user
 */
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

    /**
     * Is called by the REST Controller and given the user.
     * if the given user is not null it is saved to the repository
     * and returns http status OK, if not it returns http status BAD_REQUEST
     *
     * @param user user to add to the repository
     * @return a ResponseEntity depending on success or not
     */
    public ResponseEntity<String> createUser(User user) {
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (null == foundUser) {
            userRepository.save(user);
            return new ResponseEntity<>("User created", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Is called by the REST Controller and given the email
     * for the user in which we want to delete.
     * if the user with the specified email is found in the database
     * he will be deleted and we return a ResponseEntity with status code OK.
     * If the user is not found it will return a ResponseEntitu
     * with status code BAD_REQUEST
     *
     * @param email email for the user to delete
     * @return http status OK if successfully deleted and BAD_REQUEST if not
     */
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
