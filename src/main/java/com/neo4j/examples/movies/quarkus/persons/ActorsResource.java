package com.neo4j.examples.movies.quarkus.persons;

import com.neo4j.examples.movies.quarkus.persons.entities.ActorRecommendation;
import com.neo4j.examples.movies.quarkus.persons.entities.CoActor;
import com.neo4j.examples.movies.quarkus.persons.entities.Person;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("actors")
@Produces(MediaType.APPLICATION_JSON)
public class ActorsResource {

    @Inject
    Persons persons;

    @GET
    public List<Person> actors() {
        return persons.actors();
    }

    @GET
    @Path("{name}/movies")
    public List<MovieAct> movies(@PathParam("name") String name) {
        return persons.findActs(name).stream()
                .map(a -> new MovieAct(a.movie.title, a.roles))
                .collect(Collectors.toList());
    }

    @GET
    @Path("{name}/co-actors/")
    public List<CoActor> coActors(@PathParam("name") String name) {
        return persons.findCoActors(name).stream()
                .map(a -> new CoActor(a.person.name, a.movie.title))
                .collect(Collectors.toList());
    }

    @GET
    @Path("{first}/shortest-path/{second}")
    public List<CoActor> shortestPath(@PathParam("first") String first, @PathParam("second") String second) {
        return persons.findShortestPath(first, second);
    }

    @GET
    @Path("{name}/recommendations")
    public List<ActorRecommendation> recommendCoActor(@PathParam("name") String name) {
        return persons.recommendCoActor(name);
    }

    public static class MovieAct {

        public String title;
        public Set<String> roles = new HashSet<>();

        public MovieAct(String title, Set<String> roles) {
            this.title = title;
            this.roles.addAll(roles);
        }
    }

}
