package com.neo4j.examples.movies.quarkus.movies;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("movies")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {

    @Inject
    Movies movies;

    @GET
    @Path("{title}")
    public Movie findByTitle(@PathParam("title") String title) {
        return movies.loadByTitle(title);
    }

    @POST
    @Path("{title}/vote")
    public int voteByTitle(@PathParam("title") String title) {
        return movies.voteInMovieByTitle(title);
    }

}
