package com.imdb.moviesAPI.movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieControllerTest {
    MockMvc mockMvc;

    @MockBean
    MovieRepository movieRepository;

    @Autowired
    MovieService movieService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    Movie movie1;
    Movie movie2;
    Movie movie3;

    @BeforeEach
    void setUp() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        movie1= new Movie((long)1,"poster1","poster2","title1",new Date(),"overview1");
        movie2= new Movie((long)2,"poster1","poster2","title2",new Date(),"overview2");
        movie3= new Movie((long)3,"poster1","poster2","title3",new Date(),"overview3");

        List<Movie> movies = List.of(movie1, movie2, movie3);

        // mocking the findAll method of movieRepository
        Page<Movie> pageMock= Mockito.mock(Page.class);
        Mockito.when(pageMock.toList()).thenReturn(movies);
        Mockito.when(movieRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pageMock);

        // mocking the findById method of movieRepository
        Mockito.when(movieRepository.findById((long)1)).thenReturn(Optional.of(movie1));
        Mockito.when(movieRepository.findById((long)2)).thenReturn(Optional.of(movie2));
        Mockito.when(movieRepository.findById((long)3)).thenReturn(Optional.of(movie3));
        Mockito.when(movieRepository.findById((long)4)).thenReturn(Optional.empty());

        // mocking adding movies to the database
        Mockito.when(movieRepository.saveAll(Mockito.anyList())).thenReturn(movies);

    }

    @Test
    void getMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies?page=1")
                        .header("Authorization", "Bearer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("title1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("title2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("title3"));
    }

    @Test
    void getMovieByExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movie/1")
                        .header("Authorization", "Bearer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title1"));
    }

    @Test
    void getMovieByNotFoundId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movie/4")
                        .header("Authorization", "Bearer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }
    @Test
    void addMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/movies")
                        .header("Authorization", "Bearer")
                        .content(objectMapper.writeValueAsString(List.of(movie1, movie2, movie3)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}