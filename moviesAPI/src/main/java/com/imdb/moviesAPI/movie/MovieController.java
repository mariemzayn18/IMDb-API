package com.imdb.moviesAPI.movie;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class MovieController {

    private MovieService movieService;

    public MovieController(MovieService movieService){this.movieService = movieService;}

    @GetMapping("/movies")
    public List<Movie> getMovies(@RequestParam int page,
                                 @RequestHeader String Authentication ){
        return movieService.getMovies(page);
    }

    @GetMapping("/movie/{id}")
    public Movie getMovieById(@PathVariable Long id,
                              @RequestHeader String Authentication ){
        return movieService.getMovieById(id);
    }

    @PostMapping("/movies")
    public void addMovies(@RequestHeader String Authentication,
                          @RequestBody List<Movie> movies ){
        movieService.addMovies(movies);
    }
}
