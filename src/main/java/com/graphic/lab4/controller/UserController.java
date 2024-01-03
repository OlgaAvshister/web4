package com.graphic.lab4.controller;

import com.graphic.lab4.service.UserService;
import com.graphic.lab4.model.data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;
import java.util.regex.Pattern;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User newUser) {
        Pattern pattern = Pattern.compile(
                "[" +                   //начало списка допустимых символов
                        "a-zA-Z" +    //буквы русского алфавита
                        "\\p{Punct}" +  //знаки пунктуации
                        "]" +                   //конец списка допустимых символов
                        "*");                   //допускается наличие указанных символов в любом количестве
        if (pattern.matcher(newUser.getUsername()).matches()) {
            return new ResponseEntity<>("Unacceptable symbols", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        } else {
            if (userService.findByUsername(newUser.getUsername()) != null) {
                logger.error("username Already exist " + newUser.getUsername());
                return new ResponseEntity<>(
                        new RuntimeException("user with username " + newUser.getUsername() + " already exist "),
                        HttpStatus.CONFLICT);
            }

            logger.info("user registered " + newUser.getUsername()+" !!!!!!!!!!!!!!!!11");

            return new ResponseEntity<>(userService.save(newUser), HttpStatus.CREATED);
        }
    }

    @CrossOrigin
    @RequestMapping("/login")
    public ResponseEntity<?> user(Principal principal) {
        String name;
        if (principal != null) {
            name = principal.getName();
            logger.info("user logged " + name);
        }else {
            logger.info("Wrong username or password");
            return new ResponseEntity<>("Wrong username or password", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(name, HttpStatus.OK);
    }
}