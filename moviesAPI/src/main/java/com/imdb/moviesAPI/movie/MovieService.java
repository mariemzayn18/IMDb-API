package com.imdb.moviesAPI.movie;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.sql.Date.valueOf;

@Service
public class MovieService {
    private MovieRepository movieRepository;

    MovieService(MovieRepository movieRepository){this.movieRepository= movieRepository;}

    private List<Movie> readMoviesFromJSON() throws IOException {
        List<Movie> movies = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/imdb/moviesAPI/data/MoviesData.json"))) {
            JSONArray jsonArray = new JSONArray(reader.lines().collect(Collectors.joining()));

            jsonArray.forEach(movie -> {
                JSONObject jsonObject = (JSONObject) movie;
                LocalDate releaseDate = LocalDate.parse(jsonObject.getString("releaseDate"), DateTimeFormatter.ISO_DATE);
                movies.add(new Movie(
                        jsonObject.getLong("id"),
                        jsonObject.getString("posterPath"),
                        jsonObject.getString("backdropPath"),
                        jsonObject.getString("title"),
                        valueOf(releaseDate),
                        jsonObject.getString("overview")
                ));
            });
        }

        return movies;
    }


    public void addMovies() {
        try {
            List<Movie> movies = readMoviesFromJSON();
            movieRepository.saveAll(movies);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
