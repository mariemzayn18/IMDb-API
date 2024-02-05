package com.imdb.moviesAPI.repositories.repository;
import com.imdb.moviesAPI.repositories.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
