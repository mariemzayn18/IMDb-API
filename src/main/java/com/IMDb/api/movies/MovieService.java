package com.IMDb.api.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Date.valueOf;

@Service
public class MovieService {
    private MovieRepository movieRepository;

    MovieService(MovieRepository movieRepository){this.movieRepository= movieRepository;}

    private List<Movie> readMoviesFromJSON() throws IOException {
        List<Movie> movies= new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/IMDb/api/movies/movies.json"))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            jsonArray.forEach(movie -> {
                JSONObject jsonObject = (JSONObject) movie;
                String releaseDateString = jsonObject.getString("releaseDate");
                LocalDate releaseDate = LocalDate.parse(releaseDateString, DateTimeFormatter.ISO_DATE);
                Movie m = new Movie(
                        jsonObject.getLong("id"),
                        jsonObject.getString("posterPath"),
                        jsonObject.getString("backdropPath"),
                        jsonObject.getString("title"),
                        valueOf(releaseDate),
                        jsonObject.getInt("page"),
                        jsonObject.getString("overview")
                );
                movies.add(m);
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

    public List<Movie> getMovies() {
       return movieRepository.findAll();
    }


    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

}
