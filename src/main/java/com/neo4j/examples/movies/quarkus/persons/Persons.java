package com.neo4j.examples.movies.quarkus.persons;

import com.neo4j.examples.movies.quarkus.persons.entities.Act;
import com.neo4j.examples.movies.quarkus.persons.entities.ActorRecommendation;
import com.neo4j.examples.movies.quarkus.persons.entities.CoActor;
import com.neo4j.examples.movies.quarkus.persons.entities.Person;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class Persons {

    @Inject
    SessionFactory sessionFactory;

    public List<Person> actors() {
        Session session = sessionFactory.openSession();
        Iterable<Person> iterable = session.query(Person.class, "MATCH (actor:Person)-[:ACTED_IN]-(movie) RETURN DISTINCT actor ORDER BY actor.name", Map.of());
        return resultList(iterable);
    }

    public List<Act> findCoActors(String name) {
        Session session = sessionFactory.openSession();
        Iterable<Act> iterable = session.query(Act.class, "MATCH (actor:Person {name: $name})-[:ACTED_IN]->(m)<-[a:ACTED_IN]-(coActor) RETURN a, m, coActor", Map.of("name", name));
        return resultList(iterable);
    }

    private static <T> List<T> resultList(Iterable<T> result) {
        ArrayList<T> list = new ArrayList<>();
        result.forEach(list::add);
        return list;
    }

    public List<CoActor> findShortestPath(String first, String second) {
        Session session = sessionFactory.openSession();
        Result result = session.query("MATCH (first:Person {name: $first}), " +
                                      "(second:Person {name: $second}), " +
                                      "p=shortestPath((first)-[*]-(second)) " +
                                      "RETURN p", Map.of("first", first, "second", second), true);

        Iterator<Map<String, Object>> iterator = result.iterator();
        List<CoActor> path = new ArrayList<>();
        while (iterator.hasNext()) {
            Map<String, Object> object = iterator.next();
            Path.Segment[] p = (Path.Segment[]) object.get("p");
            for (Path.Segment segment : p) {
                Node actorNode = segment.relationship().startNodeId() == segment.start().id()
                        ? segment.start() : segment.end();
                Node movieNode = segment.start().id() == actorNode.id()
                        ? segment.end() : segment.start();

                String actor = actorNode.get("name").asString();
                String movie = movieNode.get("title").asString();
                path.add(new CoActor(actor, movie));
            }
        }

        return path;
    }

    public List<ActorRecommendation> recommendCoActor(String name) {
        Session session = sessionFactory.openSession();
        Result result = session.query("MATCH (actor:Person {name: $name})-[:ACTED_IN]->(m)<-[:ACTED_IN]-(coActors), " +
                                      " (coActors)-[:ACTED_IN]->(m2)<-[:ACTED_IN]-(cocoActors) " +
                                      " WHERE NOT (actor)-[:ACTED_IN]->()<-[:ACTED_IN]-(cocoActors)" +
                                      " AND actor <> cocoActors " +
                                      " RETURN cocoActors.name AS recommended, count(*) AS strength ORDER BY strength DESC", Map.of("name", name), true);

        Iterator<Map<String, Object>> iterator = result.iterator();
        List<ActorRecommendation> recommendations = new ArrayList<>();
        while (iterator.hasNext()) {
            Map<String, Object> object = iterator.next();
            recommendations.add(new ActorRecommendation((String) object.get("recommended"), (long) object.get("strength")));
        }

        return recommendations;
    }

    public List<Act> findActs(String name) {
        Session session = sessionFactory.openSession();
        Iterable<Act> iterable = session.query(Act.class, "MATCH (actor:Person {name: $name})-[a:ACTED_IN]->(m) RETURN actor, a, m", Map.of("name", name));
        return resultList(iterable);
    }

}
