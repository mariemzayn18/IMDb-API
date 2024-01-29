package com.IMDb.api.movies.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    @Id
    private Long id;
    private String characterName;
    private String name;
    private String profilePath;
}
