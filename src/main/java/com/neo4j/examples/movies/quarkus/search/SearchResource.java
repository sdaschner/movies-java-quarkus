package com.neo4j.examples.movies.quarkus.search;

import com.neo4j.examples.movies.quarkus.movies.entities.Movie;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {

    @Inject
    Searches searches;

    @GET
    public List<Movie> search(@QueryParam("q") @DefaultValue("") String title) {
        return searches.searchMoviesByTitle(stripWildcards(title));
    }

    private static String stripWildcards(String title) {
        String result = title;
        if (result.startsWith("*")) {
            result = result.substring(1);
        }
        if (result.endsWith("*")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    @GET
    @Path("latest")
    public List<Movie> latestMovies() {
        return searches.latestMovies();
    }

    @GET
    @Path("90s-movies")
    public List<Movie> scenario90sMovies() {
        return searches.search90sMovies();
    }

    @GET
    @Path("tom-hanks-movies")
    public List<Movie> scenarioTomHanksMovies() {
        return searches.searchTomHanksMovies();
    }

}
