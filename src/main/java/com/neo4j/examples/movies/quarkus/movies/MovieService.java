package com.neo4j.examples.movies.quarkus.movies;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MovieService {

    @Inject
    SessionFactory sessionFactory;

    public MovieDetails fetchDetailsByTitle(String title) {
        Session session = sessionFactory.openSession();
        Result result = session.query("MATCH (movie:Movie {title: $title}) " +
                                      "OPTIONAL MATCH (person:Person)-[r]->(movie) " +
                                      "WITH movie, COLLECT({ name: person.name, job: REPLACE(TOLOWER(TYPE(r)), '_in', ''), role: HEAD(r.roles) }) as cast " +
                                      "RETURN movie { .title, cast: cast }"
                , Map.of("title", title), true);

        Iterator<Map<String, Object>> iterator = result.iterator();
        if (!iterator.hasNext()) return null;

        return new MovieDetails(iterator.next());
    }

    public int voteInMovieByTitle(String title) {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        try {
            Result result = session.query("MATCH (m:Movie {title: $title}) " +
                                          "WITH m, coalesce(m.votes, 0) AS currentVotes " +
                                          "SET m.votes = currentVotes + 1;",
                    Map.of("title", title));
            transaction.commit();
            transaction.close();
            return result.queryStatistics().getPropertiesSet();
        } catch (RuntimeException e) {
            System.err.println("Could not execute transaction: " + e);
            transaction.rollback();
            throw e;
        }
    }

    public List<Movie> searchMoviesByTitle(String title) {
        Session session = sessionFactory.openSession();
        Iterable<Movie> iterable = session.query(Movie.class, "MATCH (movie:Movie) WHERE movie.title CONTAINS $title RETURN movie", Map.of("title", title));
        return resultList(iterable);
    }

    public MovieGraph fetchMovieGraph() {
        Session session = sessionFactory.openSession();
        Result result = session.query(" MATCH (m:Movie) <- [r:ACTED_IN] - (p:Person)"
                                      + " WITH m, p ORDER BY m.title, p.name"
                                      + " RETURN m.title AS movie, collect(p.name) AS actors"
                , Map.of(), true);

        return new MovieGraph(result);
    }

    private static <T> List<T> resultList(Iterable<T> result) {
        ArrayList<T> list = new ArrayList<>();
        result.forEach(list::add);
        return list;
    }

}
