package com.neo4j.examples.movies.quarkus.graph;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/graph")
@Produces(MediaType.APPLICATION_JSON)
public class GraphResource {

    @Inject
    Graphs graphs;

    @GET
    public MovieGraph getGraph() {
        return graphs.fetchMovieGraph();
    }

}
