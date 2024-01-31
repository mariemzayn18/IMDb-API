package com.imdb.validations.user;

import org.springframework.stereotype.Service;

@Service
public class ValidationUserService {

    private ValidationUserRepository validationUserRepository;

    public ValidationUserService(ValidationUserRepository validationUserRepository) {
        this.validationUserRepository = validationUserRepository;
    }


    public void register(ValidationUsers validationUsers) {
        validationUserRepository.save(validationUsers);
    }

    public void updateInfo(ValidationUsers validationUsers) {
        validationUserRepository.save(validationUsers);
    }

    public boolean isUserLoggedIn(String email){
        ValidationUsers validationUsers = validationUserRepository.findById(email).orElse(null);
        return validationUsers != null ? validationUsers.getIsLoggedIn() : false;
    }
}
