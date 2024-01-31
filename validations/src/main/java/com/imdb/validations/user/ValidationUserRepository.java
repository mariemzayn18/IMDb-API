package com.imdb.validations.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationUserRepository extends JpaRepository<ValidationUsers, String> {
}
