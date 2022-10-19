package com.neo4j.examples.movies.quarkus.graph;

import com.neo4j.examples.movies.quarkus.Neo4jTestResource;
import com.neo4j.examples.movies.quarkus.graph.Graphs;
import com.neo4j.examples.movies.quarkus.graph.MovieGraph;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.ogm.session.SessionFactory;

import javax.inject.Inject;
import java.util.Map;

import static com.neo4j.examples.movies.quarkus.TestData.insertTestData;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(Neo4jTestResource.class)
public class GraphsIT {

    @Inject
    SessionFactory sessionFactory;

    @Inject
    Graphs graphs;

    @BeforeEach
    void setup() {
        insertTestData(sessionFactory.openSession());
    }

    @Test
    public void fetches_d3_graph() {
        MovieGraph d3Graph = graphs.fetchMovieGraph();

        MovieGraph expected = new MovieGraph();
        expected.getLinks().add(Map.of("source", 1, "target", 0));
        expected.getNodes().add(Map.of("label", "movie", "title", "The Matrix"));
        expected.getNodes().add(Map.of("label", "actor", "title", "Keanu Reeves"));

        assertThat(expected).isEqualTo(d3Graph);
    }
}
