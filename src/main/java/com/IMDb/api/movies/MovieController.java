package com.IMDb.api.movies;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/movies")
    public ResponseEntity<String> addMovies(@RequestBody List<Movie> movies){
        try {
            movieService.addMovies();
            return new ResponseEntity<>("Movies added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding movies: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
