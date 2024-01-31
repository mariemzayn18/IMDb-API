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
    public void register(@RequestBody ValidationUser validationUser) {
        validationUserService.register(validationUser);
    }

    @PutMapping("/info")
    public void updateInfo(@RequestBody ValidationUser validationUser) {
        validationUserService.updateInfo(validationUser);
    }
}
