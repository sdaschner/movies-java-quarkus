package com.neo4j.examples.movies.quarkus.persons;

import com.neo4j.examples.movies.quarkus.movies.Movie;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Person {

    @Id
    public String name;
    public Integer born;

    @Relationship("DIRECTED")
    public Set<Movie> directed = new HashSet<>();

    @Relationship("WROTE")
    public Set<Movie> writings = new HashSet<>();

    @Relationship("PRODUCED")
    public Set<Movie> productions = new HashSet<>();

    @Relationship("REVIEWED")
    public Set<Review> reviews = new HashSet<>();

    @Relationship("ACTED_IN")
    public Set<Act> acts = new HashSet<>();

}
