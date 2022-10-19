package com.neo4j.examples.movies.quarkus.persons;

import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import java.util.Set;

public class ReviewsSerializer implements JsonbSerializer<Set<Review>> {

    @Override
    public void serialize(Set<Review> reviews, JsonGenerator generator, SerializationContext ctx) {
        generator.writeStartArray();
        reviews.forEach(review -> serializeReview(review, generator));
        generator.writeEnd();
    }

    private void serializeReview(Review review, JsonGenerator generator) {
        generator.writeStartObject();

        generator.write("reviewer", review.person.name);
        generator.write("review", review.summary);
        generator.write("rating", review.rating);

        generator.writeEnd();
    }

}
