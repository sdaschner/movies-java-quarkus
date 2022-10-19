package com.neo4j.examples.movies.quarkus.persons;

import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import java.util.Set;

public class ActsSerializer implements JsonbSerializer<Set<Act>> {

    @Override
    public void serialize(Set<Act> acts, JsonGenerator generator, SerializationContext ctx) {
        generator.writeStartArray();
        acts.forEach(act -> serializeAct(act, generator));
        generator.writeEnd();
    }

    private void serializeAct(Act act, JsonGenerator generator) {
        generator.writeStartObject();

        generator.write("actor", act.person.name);

        generator.writeStartArray("roles");
        act.roles.forEach(generator::write);
        generator.writeEnd();

        generator.writeEnd();
    }

}
