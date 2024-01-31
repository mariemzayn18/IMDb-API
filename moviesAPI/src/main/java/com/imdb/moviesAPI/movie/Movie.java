package com.imdb.moviesAPI.movie;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Getter
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
