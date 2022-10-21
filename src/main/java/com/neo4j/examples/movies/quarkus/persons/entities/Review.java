package com.neo4j.examples.movies.quarkus.persons.entities;

import com.neo4j.examples.movies.quarkus.movies.entities.Movie;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity("REVIEWED")
public class Review {

    @Id
    @GeneratedValue
    private long id;

    @StartNode
    public Person person;

    @EndNode
    public Movie movie;

    @Required
    public String summary;

    @Required
    public long rating;

}
