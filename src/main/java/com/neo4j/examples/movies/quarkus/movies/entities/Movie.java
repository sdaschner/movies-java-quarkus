package com.neo4j.examples.movies.quarkus.movies.entities;

import com.neo4j.examples.movies.quarkus.persons.*;
import com.neo4j.examples.movies.quarkus.persons.entities.Act;
import com.neo4j.examples.movies.quarkus.persons.ActsSerializer;
import com.neo4j.examples.movies.quarkus.persons.entities.Person;
import com.neo4j.examples.movies.quarkus.persons.entities.Review;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import javax.json.bind.annotation.JsonbTypeSerializer;
import java.util.HashSet;
import java.util.Set;

import static org.neo4j.ogm.annotation.Relationship.Direction.INCOMING;

@NodeEntity
public class Movie {

    @Id
    public String title;
    public String tagline;
    public Integer released;
    public int votes;

    @Relationship(value = "DIRECTED", direction = INCOMING)
    @JsonbTypeSerializer(PersonNamesSerializer.class)
    public Set<Person> directors = new HashSet<>();

    @Relationship(value = "WROTE", direction = INCOMING)
    @JsonbTypeSerializer(PersonNamesSerializer.class)
    public Set<Person> writers = new HashSet<>();

    @Relationship(value = "PRODUCED", direction = INCOMING)
    @JsonbTypeSerializer(PersonNamesSerializer.class)
    public Set<Person> producers = new HashSet<>();

    @Relationship(value = "REVIEWED", direction = INCOMING)
    @JsonbTypeSerializer(ReviewsSerializer.class)
    public Set<Review> reviewers = new HashSet<>();

    @Relationship(value = "ACTED_IN", direction = INCOMING)
    @JsonbTypeSerializer(ActsSerializer.class)
    public Set<Act> actors = new HashSet<>();

}
