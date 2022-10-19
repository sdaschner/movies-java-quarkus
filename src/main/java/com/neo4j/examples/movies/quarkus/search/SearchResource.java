package com.neo4j.examples.movies.quarkus.search;

import com.neo4j.examples.movies.quarkus.movies.Movie;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {

    @Inject
    Searches searches;

    @GET
    public List<Movie> search(@QueryParam("q") String title) {
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

}
