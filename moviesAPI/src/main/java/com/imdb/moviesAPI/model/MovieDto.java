package com.imdb.moviesAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private Long id;
    private String posterPath;
    private String backdropPath;
    private String title;
    private Date releaseDate;
    private String overview;
    private double score;
}
