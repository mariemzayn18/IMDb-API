package com.imdb.validations.user;

import org.springframework.stereotype.Service;

@Service
public class ValidationUserService {

    private ValidationUserRepository validationUserRepository;

    public ValidationUserService(ValidationUserRepository validationUserRepository) {
        this.validationUserRepository = validationUserRepository;
    }


    public void register(ValidationUser validationUser) {
        validationUserRepository.save(validationUser);
    }

    public void updateInfo(ValidationUser validationUser) {
        validationUserRepository.save(validationUser);
    }

    public Boolean isUserLoggedIn(String email){
        ValidationUser validationUser = validationUserRepository.findById(email).orElse(null);
        return validationUser != null ? validationUser.getIsLoggedIn() : false;
    }
}
