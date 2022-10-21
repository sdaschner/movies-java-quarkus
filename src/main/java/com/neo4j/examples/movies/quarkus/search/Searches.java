package com.neo4j.examples.movies.quarkus.search;

import com.neo4j.examples.movies.quarkus.movies.Movie;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class Searches {

    @Inject
    SessionFactory sessionFactory;

    public List<Movie> searchMoviesByTitle(String title) {
        Session session = sessionFactory.openSession();
        Iterable<Movie> iterable = session.query(Movie.class, "MATCH (movie:Movie) WHERE movie.title CONTAINS $title RETURN movie", Map.of("title", title));
        return resultList(iterable);
    }

    private static <T> List<T> resultList(Iterable<T> result) {
        ArrayList<T> list = new ArrayList<>();
        result.forEach(list::add);
        return list;
    }

    public List<Movie> latestMovies() {
        Session session = sessionFactory.openSession();
        Iterable<Movie> iterable = session.query(Movie.class, "MATCH (movie:Movie) RETURN movie ORDER BY movie.released DESC LIMIT 5", Map.of());
        return resultList(iterable);
    }

    public List<Movie> search90sMovies() {
        Session session = sessionFactory.openSession();
        Iterable<Movie> iterable = session.query(Movie.class, "MATCH (movie:Movie) WHERE movie.released >= 1990 AND movie.released < 2000 RETURN movie ORDER BY movie.released DESC", Map.of());
        return resultList(iterable);
    }

    public List<Movie> searchTomHanksMovies() {
        Session session = sessionFactory.openSession();
        Iterable<Movie> iterable = session.query(Movie.class, "MATCH (tom:Person {name: 'Tom Hanks'})-[:ACTED_IN]->(movie) RETURN movie ORDER BY movie.released DESC", Map.of());
        return resultList(iterable);
    }

}
