package com.neo4j.examples.movies.quarkus.persons.entities;

import com.neo4j.examples.movies.quarkus.movies.entities.Movie;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RelationshipEntity("ACTED_IN")
public class Act {

    @Id
    @GeneratedValue
    public long id;

    @StartNode
    public Person person;

    @EndNode
    public Movie movie;

    @Required
    public Set<String> roles = new HashSet<>();

}
