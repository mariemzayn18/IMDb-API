package com.IMDb.api.movies;


import com.IMDb.api.movies.entity.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class MovieController {

    private MovieService movieService;

    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public List<Movie> getMovies(){
        return movieService.getMovies();
    }

    @GetMapping("/movie/{id}")
    public Movie getMovieById(@PathVariable Long id){
        return movieService.getMovieById(id);
    }
}
