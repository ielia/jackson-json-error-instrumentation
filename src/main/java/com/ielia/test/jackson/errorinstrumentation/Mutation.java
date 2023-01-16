package com.ielia.test.jackson.errorinstrumentation;

import com.ielia.test.jackson.errorinstrumentation.mutagens.Mutagen;

import java.util.Objects;

public class Mutation {
    protected int mutationIndex;
    protected String description;
    protected String json;
    protected String path;
    protected Class<? extends Mutagen> mutagen;

    public Mutation(int mutationIndex, String path, Class<? extends Mutagen> mutagen, String description, String json) {
        this.mutationIndex = mutationIndex;
        this.path = path;
        this.mutagen = mutagen;
        this.description = description;
        this.json = json;
    }

    public String getDescription() { return description; }
    public String getJSON() { return json; }
    public Class<? extends Mutagen> getMutagen() { return mutagen; }
    public int getMutationIndex() { return mutationIndex; }
    public String getPath() { return path; }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Mutation)) return false;
        Mutation mutation = (Mutation) other;
        return mutationIndex == mutation.mutationIndex
                && Objects.equals(description, mutation.description)
                && Objects.equals(json, mutation.json)
                && Objects.equals(path, mutation.path)
                && Objects.equals(mutagen, mutation.mutagen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mutationIndex, description, json, path, mutagen);
    }
}
