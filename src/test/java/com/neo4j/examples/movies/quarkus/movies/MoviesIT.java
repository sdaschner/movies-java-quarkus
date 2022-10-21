package com.neo4j.examples.movies.quarkus.movies;

import com.neo4j.examples.movies.quarkus.Neo4jTestResource;
import com.neo4j.examples.movies.quarkus.movies.entities.Movie;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.ogm.session.SessionFactory;

import javax.inject.Inject;
import java.util.Set;

import static com.neo4j.examples.movies.quarkus.TestData.insertTestData;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(Neo4jTestResource.class)
class MoviesIT {

    @Inject
    SessionFactory sessionFactory;

    @Inject
    Movies service;

    @BeforeEach
    void setup() {
        insertTestData(sessionFactory.openSession());
    }

    @Test
    public void fetches_movie_details() {
        Movie movie = service.loadByTitle("The Matrix");

        assertThat(movie.title).isEqualTo("The Matrix");
        assertThat(movie.actors)
                .hasSize(1)
                .first()
                .extracting(a -> a.movie, a -> a.person.name, a -> a.roles)
                .contains(movie, "Keanu Reeves", Set.of("Neo"));
    }


}