package com.imdb.validations.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        userService.register(user);
    }

    @PutMapping("/info")
    public void updateInfo(@RequestBody User user) {
        userService.updateInfo(user);
    }
}
