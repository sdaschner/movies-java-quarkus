package com.neo4j.examples.movies.quarkus.search;

import com.neo4j.examples.movies.quarkus.Neo4jTestResource;
import com.neo4j.examples.movies.quarkus.search.Searches;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.ogm.session.SessionFactory;

import javax.inject.Inject;

import static com.neo4j.examples.movies.quarkus.TestData.insertTestData;

@QuarkusTest
@QuarkusTestResource(Neo4jTestResource.class)
public class SearchesIT {

    @Inject
    SessionFactory sessionFactory;

    @Inject
    Searches searches;

    @BeforeEach
    void setup() {
        insertTestData(sessionFactory.openSession());
    }

    @Test
    public void searches_movies_by_title() {
        String title = "Matrix Re";
        Assertions.assertThat(searches.searchMoviesByTitle(title))
                .hasSize(2)
                .extracting(mr -> mr.title).containsExactlyInAnyOrder("The Matrix Reloaded", "The Matrix Revolutions");
    }

}
