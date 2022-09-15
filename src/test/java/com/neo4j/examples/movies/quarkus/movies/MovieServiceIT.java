package com.neo4j.examples.movies.quarkus.movies;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;

import javax.inject.Inject;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(Neo4jTestResource.class)
class MovieServiceIT {

    @Inject
    SessionFactory sessionFactory;

    @Inject
    MovieService service;

    @BeforeEach
    void setup() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try (transaction) {
            session.query("MATCH (n) DETACH DELETE n", Map.of());
            session.query("CREATE (TheMatrix:Movie {title:'The Matrix', released:1999, tagline:'Welcome to the Real World'})\n"
                          + "CREATE (TheMatrixReloaded:Movie {title:'The Matrix Reloaded', released:2003, tagline:'Free your mind'})\n"
                          + "CREATE (TheMatrixRevolutions:Movie {title:'The Matrix Revolutions', released:2003, tagline:'Everything that has a beginning has an end'})\n"
                          + "CREATE (p:Person {name: 'Keanu Reeves', born: 1964}) -[:ACTED_IN {roles: ['Neo']}]-> (TheMatrix)\n", Map.of());
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Test
    public void searches_movies_by_title() {
        String title = "Matrix Re";
        Assertions.assertThat(service.searchMoviesByTitle(title))
                .hasSize(2)
                .extracting(mr -> mr.title).containsExactlyInAnyOrder("The Matrix Reloaded", "The Matrix Revolutions");
    }

    @Test
    public void fetches_movie_details() {
        MovieDetails details = service.fetchDetailsByTitle("The Matrix");

        assertThat(details.title).isEqualTo("The Matrix");
        assertThat(details.getCast()).containsExactly(new CastMember("Keanu Reeves", "acted", "Neo"));
    }

    @Test
    public void fetches_d3_graph() {
        MovieGraph d3Graph = service.fetchMovieGraph();

        MovieGraph expected = new MovieGraph();
        expected.getLinks().add(Map.of("source", 1, "target", 0));
        expected.getNodes().add(Map.of("label", "movie", "title", "The Matrix"));
        expected.getNodes().add(Map.of("label", "actor", "title", "Keanu Reeves"));

        assertThat(expected).isEqualTo(d3Graph);
    }

}