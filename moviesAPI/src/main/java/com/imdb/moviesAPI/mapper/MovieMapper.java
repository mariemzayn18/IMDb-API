package com.imdb.moviesAPI.mapper;

import com.imdb.moviesAPI.model.MovieDto;
import com.imdb.moviesAPI.repositories.entity.Movie;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(target = "movieDto.score", ignore = true)
    List<Movie> mapMovieDtoToMovie(List<MovieDto> movieDto);

    @Mapping(target = "movieDto.score", ignore = true)
    Movie map(MovieDto movieDto);

}
