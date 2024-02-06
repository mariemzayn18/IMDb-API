package com.imdb.moviesAPI.mapper;

import com.imdb.moviesAPI.model.MovieDto;
import com.imdb.moviesAPI.repositories.entity.Movie;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    Movie map(MovieDto movieDto);

    List<Movie> mapMovieDtoToMovie(List<MovieDto> movieDtoList);

}
