package com.neo4j.examples.movies.quarkus.movies;

import java.util.Objects;

public class CastMember {

    public String name;
    public String job;
    public String role;

    public CastMember() {
    }

    CastMember(String name, String job, String role) {
        this.name = name;
        this.job = job;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastMember that = (CastMember) o;
        return Objects.equals(name, that.name) && Objects.equals(job, that.job) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, job, role);
    }

}
