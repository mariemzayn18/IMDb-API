package com.imdb.validations.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationUsers {
    @Id
    private String email;
    private Boolean isLoggedIn;
}
