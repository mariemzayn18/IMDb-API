package com.IMDb.api.movies.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @Id
    private String id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                           CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "movie_genre",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> movies;
}
