package com.IMDb.api.movies;

import com.IMDb.api.movies.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
