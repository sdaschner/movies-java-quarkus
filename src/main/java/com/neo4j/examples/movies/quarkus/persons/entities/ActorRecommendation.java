package com.neo4j.examples.movies.quarkus.persons.entities;

public class ActorRecommendation {

    public String actor;
    public long strength;

    public ActorRecommendation(String actor, long strength) {
        this.actor = actor;
        this.strength = strength;
    }

}
