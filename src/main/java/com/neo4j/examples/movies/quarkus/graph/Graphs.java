package com.neo4j.examples.movies.quarkus.graph;

import com.neo4j.examples.movies.quarkus.graph.entities.MovieGraph;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

@ApplicationScoped
public class Graphs {

    @Inject
    SessionFactory sessionFactory;

    public MovieGraph fetchMovieGraph() {
        Session session = sessionFactory.openSession();
        Result result = session.query(" MATCH (m:Movie) <- [r:ACTED_IN] - (p:Person)"
                                      + " WITH m, p ORDER BY m.title, p.name"
                                      + " RETURN m.title AS movie, collect(p.name) AS actors"
                , Map.of(), true);

        return new MovieGraph(result);
    }

}
