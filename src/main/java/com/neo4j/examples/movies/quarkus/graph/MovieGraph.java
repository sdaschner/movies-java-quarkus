package com.neo4j.examples.movies.quarkus.graph;

import org.neo4j.ogm.model.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a movie graph in the format expected by the d3js frontend.
 */
public class MovieGraph {

    private final List<Map<String, String>> nodes = new ArrayList<>();
    private final List<Map<String, Integer>> links = new ArrayList<>();

    public MovieGraph() {
    }

    MovieGraph(Result result) {
        result.forEach(this::addMovieRecord);
    }

    public List<Map<String, String>> getNodes() {
        return nodes;
    }

    public List<Map<String, Integer>> getLinks() {
        return links;
    }

    private void addMovieRecord(Map<String, Object> record) {
        Map<String, String> movie = Map.of("label", "movie", "title", (String) record.get("movie"));

        int targetIndex = nodes.size();
        nodes.add(movie);

        Stream.of((String[]) record.get("actors")).forEach(name -> {
            Map<String, String> actor = Map.of("label", "actor", "title", name);

            int sourceIndex;
            if (nodes.contains(actor)) {
                sourceIndex = nodes.indexOf(actor);
            } else {
                nodes.add(actor);
                sourceIndex = nodes.size() - 1;
            }
            links.add(Map.of("source", sourceIndex, "target", targetIndex));
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieGraph that = (MovieGraph) o;
        return nodes.equals(that.nodes) && links.equals(that.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, links);
    }
}
