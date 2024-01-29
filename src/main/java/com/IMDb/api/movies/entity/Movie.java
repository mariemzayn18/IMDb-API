package com.IMDb.api.movies.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    private Long id;
    private String posterPath;
    private String backdropPath;
    private String title;
    private Date releaseDate;
    private String overview;
    private Integer page;

    @ManyToMany(fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                           CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genre;
}
