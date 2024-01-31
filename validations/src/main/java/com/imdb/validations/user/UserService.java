package com.imdb.validations.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void register(User user) {
        userRepository.save(user);
    }

    public void updateInfo(User user) {
        userRepository.save(user);
    }

    public Boolean isUserLoggedIn(String email){
        User user= userRepository.findById(email).orElse(null);
        return user != null ? user.getIsLoggedIn() : false;
    }
}
