package com.imdb.moviesAPI.service;
import com.imdb.moviesAPI.client.AuthClient;
import com.imdb.moviesAPI.exception.AuthClientException;
import com.imdb.moviesAPI.repositories.entity.Movie;
import com.imdb.moviesAPI.repositories.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private MovieRepository movieRepository;
    private final AuthClient authClient;

    @Autowired
    MovieService(MovieRepository movieRepository, AuthClient authClient){
        this.movieRepository= movieRepository;
        this.authClient = authClient;
    }

    public void addMovies(List<Movie> movies, String authorization){
        ResponseEntity<String> valid= authClient.validate(authorization);
        movieRepository.saveAll(movies);
    }

    public List<Movie> getMovies(int page, String authorization) {
        int currentPage=page -1;
        Pageable pageable = PageRequest.of(currentPage, 8);
        ResponseEntity<String> valid= authClient.validate(authorization);
        return movieRepository.findAll(pageable).toList();
    }


    public Movie getMovieById(Long id, String authorization) {
        ResponseEntity<String> valid= authClient.validate(authorization);

        return movieRepository.findById(id).orElse(null);
    }

}
