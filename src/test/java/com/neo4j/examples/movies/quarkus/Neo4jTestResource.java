package com.neo4j.examples.movies.quarkus;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.Neo4jContainer;

import java.util.Map;

public class Neo4jTestResource implements QuarkusTestResourceLifecycleManager {

    private static final String PASSWORD = "foobar";

    private Neo4jContainer<?> container;

    @Override
    public Map<String, String> start() {
        container = new Neo4jContainer<>("neo4j:4.2").withAdminPassword(PASSWORD);

        container.start();

        return Map.of(
                "quarkus.neo4j.authentication.username", "neo4j",
                "quarkus.neo4j.authentication.password", PASSWORD,
                "quarkus.neo4j.uri", container.getBoltUrl());
    }

    @Override
    public void init(Map<String, String> initArgs) {
        QuarkusTestResourceLifecycleManager.super.init(initArgs);
    }

    @Override
    public void stop() {
        container.stop();
    }

}
