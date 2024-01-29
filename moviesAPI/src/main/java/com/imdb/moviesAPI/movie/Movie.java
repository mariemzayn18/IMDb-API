package com.imdb.moviesAPI.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
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
