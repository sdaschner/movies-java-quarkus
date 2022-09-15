package com.neo4j.examples.movies.quarkus;

import com.neo4j.examples.movies.quarkus.movies.Movie;
import com.neo4j.examples.movies.quarkus.movies.MovieDetails;
import com.neo4j.examples.movies.quarkus.movies.MovieGraph;
import com.neo4j.examples.movies.quarkus.movies.MovieService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class RootResource {

    @Inject
    MovieService movieService;

    @GET
    @Path("/movie/{title}")
    public MovieDetails findByTitle(@PathParam("title") String title) {
        return movieService.fetchDetailsByTitle(title);
    }

    @POST
    @Path("/movie/{title}/vote")
    public int voteByTitle(@PathParam("title") String title) {
        return movieService.voteInMovieByTitle(title);
    }

    @GET
    @Path("/search")
    public List<Movie> search(@QueryParam("q") String title) {
        return movieService.searchMoviesByTitle(stripWildcards(title));
    }

    @GET
    @Path("/graph")
    public MovieGraph getGraph() {
        return movieService.fetchMovieGraph();
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
