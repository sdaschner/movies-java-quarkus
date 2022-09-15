package com.neo4j.examples.movies.quarkus.movies;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Movie {

    @Id
    public String title;
    public String tagline;
    public Integer released;
    public Long votes;

}
