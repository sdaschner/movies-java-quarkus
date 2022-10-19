package com.neo4j.examples.movies.quarkus.movies;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

@ApplicationScoped
public class Movies {

    @Inject
    SessionFactory sessionFactory;

    public Movie loadByTitle(String title) {
        Session session = sessionFactory.openSession();
//        Result result = session.query("MATCH (movie:Movie {title: $title}) " +
//                                      "OPTIONAL MATCH (person:Person)-[r]->(movie) " +
//                                      "WITH movie, COLLECT({ name: person.name, job: REPLACE(TOLOWER(TYPE(r)), '_in', ''), role: HEAD(r.roles) }) as cast " +
//                                      "RETURN movie { .title, cast: cast }"

        return session.load(Movie.class, title, 1);
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

}