package com.neo4j.examples.movies.quarkus.movies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieDetails {

    public String title;
    private final List<CastMember> cast = new ArrayList<>();

    public MovieDetails() {
    }

    @SuppressWarnings("unchecked")
    MovieDetails(Map<String, Object> record) {
        Map<String, Object> movie = (Map<String, Object>) record.get("movie");
        title = (String) movie.get("title");

        var list = (List<Map<String, Object>>) movie.get("cast");
        list.forEach(r -> {
            CastMember member = new CastMember((String) r.get("name"), (String) r.get("job"), (String) r.get("role"));
            cast.add(member);
        });
    }

    public List<CastMember> getCast() {
        return cast;
    }

}
