package com.imdb.validations.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class ValidationUserController {

    private ValidationUserService validationUserService;

    public ValidationUserController(ValidationUserService validationUserService) {
        this.validationUserService = validationUserService;
    }

    @PostMapping("/register")
    public void register(@RequestBody ValidationUsers validationUsers) {
        validationUserService.register(validationUsers);
    }

    @PutMapping("/info")
    public void updateInfo(@RequestBody ValidationUsers validationUsers) {
        validationUserService.updateInfo(validationUsers);
    }
}
