/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.*;

/**
 * An implementation of Graph using a list of Vertex objects.
 *
 * The graph is represented as:
 *  - A list of Vertex objects
 *  - Each Vertex has a name and a map of outgoing edges (targets)
 *
 * PS2 instructions: MUST use the provided rep.
 */
public class ConcreteVerticesGraph implements Graph<String> {

    private final List<Vertex> vertices = new ArrayList<>();

    // Abstraction function:
    //   AF(vertices) = a directed graph G where:
    //      - Each Vertex v in the list represents a node named v.name
    //      - For each vertex v, and for each (t, w) in v.targets:
    //              there is an edge (v.name -> t) with weight w
    //
    // Representation invariant:
    //   - No duplicate vertex names in vertices list
    //   - No null names
    //   - All edge weights > 0
    //   - Every target referenced by any vertex must also appear as a vertex in vertices list
    //
    // Safety from rep exposure:
    //   - vertices field is private and final
    //   - Vertex is a private internal class; no outsider can mutate internals directly
    //   - vertices() returns an unmodifiable copy, not the actual list
    //   - maps returned by sources() and targets() are defensive copies

    // Constructor
    public ConcreteVerticesGraph() {
        checkRep();
    }

    // Check rep invariant
    private void checkRep() {
        Set<String> names = new HashSet<>();
        for (Vertex v : vertices) {
            assert v.getName() != null;
            assert !names.contains(v.getName()) : "duplicate vertex";
            names.add(v.getName());

            for (Map.Entry<String, Integer> e : v.getTargets().entrySet()) {
                assert e.getValue() > 0;
                assert names.contains(e.getKey()) ||
                        containsVertex(e.getKey()) : "referenced vertex missing";
            }
        }
    }

    private boolean containsVertex(String name) {
        for (Vertex v : vertices) {
            if (v.getName().equals(name)) return true;
        }
        return false;
    }

    private Vertex getVertex(String name) {
        for (Vertex v : vertices)
            if (v.getName().equals(name))
                return v;
        return null;
    }

    @Override
    public boolean add(String vertex) {
        if (containsVertex(vertex)) return false;
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }

    @Override
    public int set(String source, String target, int weight) {
        if (!containsVertex(source)) add(source);
        if (!containsVertex(target)) add(target);

        Vertex src = getVertex(source);
        int prev = src.setEdge(target, weight);

        checkRep();
        return prev;
    }

    @Override
    public boolean remove(String vertex) {
        Vertex v = getVertex(vertex);
        if (v == null) return false;

        // Remove any edges pointing TO the vertex
        for (Vertex x : vertices) {
            x.removeEdge(vertex);
        }

        vertices.remove(v);

        checkRep();
        return true;
    }

    @Override
    public Set<String> vertices() {
        Set<String> vs = new HashSet<>();
        for (Vertex v : vertices)
            vs.add(v.getName());
        return Collections.unmodifiableSet(vs);
    }

    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> result = new HashMap<>();
        for (Vertex v : vertices) {
            Integer w = v.getTargets().get(target);
            if (w != null) result.put(v.getName(), w);
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public Map<String, Integer> targets(String source) {
        Vertex v = getVertex(source);
        if (v == null) return Collections.emptyMap();
        return Collections.unmodifiableMap(new HashMap<>(v.getTargets()));
    }

    @Override
    public String toString() {
        return "Vertices: " + vertices() + "\nEdges: " + vertices;
    }
}

/**
 * A mutable Vertex in a directed graph.
 *
 * Internal to ConcreteVerticesGraph.
 */
class Vertex {

    private final String name;
    private final Map<String, Integer> targets = new HashMap<>();

    // Abstraction function:
    //   AF(name, targets) = a graph node named 'name' with outgoing edges
    //   to each key in targets with weight targets.get(key)
    //
    // Rep invariant:
    //   - Name is non-null
    //   - All weights in targets > 0
    //
    // Safety from rep exposure:
    //   - Name is private + immutable
    //   - Targets is private
    //   - getTargets() returns unmodifiable view

    public Vertex(String name) {
        this.name = Objects.requireNonNull(name);
        checkRep();
    }

    private void checkRep() {
        assert name != null;
        for (Integer w : targets.values())
            assert w != null && w > 0;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getTargets() {
        return Collections.unmodifiableMap(targets);
    }

    /**
     * Set edge name -> target with weight.
     * If weight = 0, edge is removed.
     * Returns previous weight or 0 if none.
     */
    public int setEdge(String target, int weight) {
        int prev = targets.getOrDefault(target, 0);

        if (weight == 0) {
            targets.remove(target);
        } else {
            targets.put(target, weight);
        }

        checkRep();
        return prev;
    }

    public void removeEdge(String target) {
        targets.remove(target);
        checkRep();
    }

    @Override
    public String toString() {
        return name + "->" + targets;
    }
}