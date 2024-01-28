package com.example.IMDbAPI.repository;

import com.example.IMDbAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
