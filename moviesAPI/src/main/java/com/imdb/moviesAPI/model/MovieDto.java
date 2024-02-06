package com.imdb.moviesAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
     String posterPath;
     String backdropPath;
     String title;
     Date releaseDate;
     String overview;
     double score;

}
