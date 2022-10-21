package com.neo4j.examples.movies.quarkus.persons;

import com.neo4j.examples.movies.quarkus.persons.entities.Person;

import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import java.util.Set;

public class PersonNamesSerializer implements JsonbSerializer<Set<Person>> {

    @Override
    public void serialize(Set<Person> persons, JsonGenerator generator, SerializationContext ctx) {
        generator.writeStartArray();
        persons.stream().map(p -> p.name).forEach(generator::write);
        generator.writeEnd();
    }

}
