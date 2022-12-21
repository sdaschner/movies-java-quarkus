package com.neo4j.examples.movies.quarkus.persons.entities;

/**
 * <strong>NOTE:</strong> As of Neo4j OGM Quarkus {@code 2.0.0}, DTO mapping classes must have a public, no-arg constructor.
 */
public class ActorRecommendation {

    public String actor;
    public long strength;

}