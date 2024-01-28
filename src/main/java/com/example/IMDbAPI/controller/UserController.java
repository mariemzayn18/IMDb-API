package com.example.IMDbAPI.controller;

import com.example.IMDbAPI.entity.User;
import com.example.IMDbAPI.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @GetMapping("/user/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
