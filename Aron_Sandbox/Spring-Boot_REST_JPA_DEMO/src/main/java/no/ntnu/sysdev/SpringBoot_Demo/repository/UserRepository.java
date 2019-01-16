package no.ntnu.sysdev.SpringBoot_Demo.repository;

import no.ntnu.sysdev.SpringBoot_Demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    List<User> findAll();
}