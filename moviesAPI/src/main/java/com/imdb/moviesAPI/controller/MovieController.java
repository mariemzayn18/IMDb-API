package com.imdb.moviesAPI.controller;

import com.imdb.moviesAPI.repositories.entity.Movie;
import com.imdb.moviesAPI.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class MovieController {

    private MovieService movieService;

    public MovieController(MovieService movieService){this.movieService = movieService;}

    @GetMapping("/movies")
    public List<Movie> getMovies(@RequestParam int page,
                                 @RequestHeader String Authorization ){
        return movieService.getMovies(page, Authorization);
    }

    @GetMapping("/movie/{id}")
    public Movie getMovieById(@PathVariable Long id,
                              @RequestHeader String Authorization ) {
        return movieService.getMovieById(id, Authorization);
    }

    @PostMapping("/movies")
    public void addMovies(@RequestHeader String Authorization,
                          @RequestBody List<Movie> movies ) {
        movieService.addMovies(movies, Authorization);
    }
}
