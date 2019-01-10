package no.ntnu.sysdev.SpringBoot_Demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.sysdev.SpringBoot_Demo.entity.User;
import no.ntnu.sysdev.SpringBoot_Demo.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost")
public class RESTController {

    private final UserService userService;
    private ObjectMapper objectMapper;

    @Autowired
    public RESTController(UserService userService) {
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/test")
    public String restTest() {
        return "Successful Test";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<String> getUsers() {
        try {
            String listOfUsers = objectMapper.writeValueAsString(userService.getAllUsers());
            return new ResponseEntity<>(listOfUsers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(HttpEntity<String> httpEntity) {
        String body = httpEntity.getBody();
        if (null != body) {
            try {
                JSONObject jsonObject = new JSONObject(body);
                return userService.createUser(new User(
                        jsonObject.getString("name"),
                        jsonObject.getString("email"),
                        jsonObject.getString("phone"),
                        jsonObject.getInt("age")
                ));
            } catch (JSONException e) {
                e.printStackTrace();
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("JSON body can't be null", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public ResponseEntity<String> deleteUser(HttpEntity<String> httpEntity) {
        String body = httpEntity.getBody();
        if (null != body) {
            try {
                JSONObject jsonObject = new JSONObject(body);
                String email = jsonObject.getString("email");
                userService.deleteUser(email);
                return new ResponseEntity<>("User deleted", HttpStatus.OK);
            } catch (JSONException e) {
                e.printStackTrace();
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("JSON body can't be null", HttpStatus.BAD_REQUEST);
    }

}
