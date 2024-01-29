package com.imdb.authenticationAPI.user;

import com.imdb.authenticationAPI.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
