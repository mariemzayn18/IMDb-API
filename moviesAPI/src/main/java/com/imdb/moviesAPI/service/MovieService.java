package com.imdb.moviesAPI.service;
import com.imdb.moviesAPI.repositories.entity.Movie;
import com.imdb.moviesAPI.repositories.repository.MovieRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private MovieRepository movieRepository;

    MovieService(MovieRepository movieRepository){this.movieRepository= movieRepository;}

    public void addMovies( List<Movie> movies) {
            movieRepository.saveAll(movies);
    }

    public List<Movie> getMovies(int page) {
        int currentPage=page -1;
        Pageable pageable = PageRequest.of(currentPage, 8);
        return movieRepository.findAll(pageable).toList();
    }


    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

}
