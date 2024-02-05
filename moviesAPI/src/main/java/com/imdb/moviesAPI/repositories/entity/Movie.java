package com.imdb.moviesAPI.repositories.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    private Long id;
    private String posterPath;
    private String backdropPath;
    private String title;
    private Date releaseDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String overview;

}
