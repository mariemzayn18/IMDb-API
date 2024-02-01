package com.imdb.validations.repositories.repository;

import com.imdb.validations.repositories.entity.ValidationUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationUserRepository extends JpaRepository<ValidationUsers, String> {
}
