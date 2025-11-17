/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * An implementation of Graph.
 *
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph implements Graph<String> {

    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();

    // Abstraction function:
    //   AF(vertices, edges) =
    //     a directed weighted graph whose set of vertices is exactly `vertices`,
    //     and whose edges contain exactly these fields: (source, target, weight)
    //
    // Representation invariant:
    //   - No null vertex appears in vertices.
    //   - In edges:
    //       * e.source and e.target are never null
    //       * e.weight > 0
    //       * e.source and e.target are in vertices
    //   - For any two vertices, there is only one edge between them
    //
    // Safety from rep exposure:
    //   - vertices and edges fields are private and final.
    //   - vertices() returns a defensive copy.
    //   - sources() and targets() return new maps, not internal ones.
    //   - Edge is immutable, and references to internal Edge objects are never returned.

    // to validate the rep invariant
    private void checkRep() {
        for (String v : vertices) {
            assert v != null;
        }
        for (Edge e : edges) {
            assert e.getSource() != null;
            assert e.getTarget() != null;
            assert vertices.contains(e.getSource());
            assert vertices.contains(e.getTarget());
            assert e.getWeight() > 0;
        }
    }

    @Override
    public boolean add(String vertex) {
        if (vertex == null) return false;
        boolean added = vertices.add(vertex);
        checkRep();
        return added;
    }

    @Override
    public int set(String source, String target, int weight) {
        if (source == null || target == null)
            throw new IllegalArgumentException("null vertices not allowed");

        // add the vertices if they dont exist
        // the specification mentions this
        vertices.add(source);
        vertices.add(target);

        // in case an edge already exists
        Edge existing = null;
        for (Edge e : edges) {
            if (e.getSource().equals(source) && e.getTarget().equals(target)) {
                existing = e;
                break;
            }
        }

        int previous = (existing == null ? 0 : existing.getWeight());

        // remove the existing edge if it exists
        if (existing != null) {
            edges.remove(existing);
        }

        // add the new edge
        if (weight > 0) {
            edges.add(new Edge(source, target, weight));
        }

        checkRep();
        return previous;
    }

    @Override
    public boolean remove(String vertex) {
        if (!vertices.contains(vertex)) return false;

        vertices.remove(vertex);

        // remove edges involving the vertex
        edges.removeIf(e -> e.getSource().equals(vertex) || e.getTarget().equals(vertex));

        checkRep();
        return true;
    }

    @Override
    public Set<String> vertices() {
        return new HashSet<>(vertices);  // defensive copy
    }

    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> map = new HashMap<>();
        for (Edge e : edges) {
            if (e.getTarget().equals(target)) {
                map.put(e.getSource(), e.getWeight());
            }
        }
        return map; // defensive
    }

    @Override
    public Map<String, Integer> targets(String source) {
        Map<String, Integer> map = new HashMap<>();
        for (Edge e : edges) {
            if (e.getSource().equals(source)) {
                map.put(e.getTarget(), e.getWeight());
            }
        }
        return map; // defensive
    }

    @Override
    public String toString() {
        return "Vertices: " + vertices + ", Edges: " + edges;
    }
}

/**
 * Immutable edge of a directed weighted graph.
 */
class Edge {

    private final String source;
    private final String target;
    private final int weight;

    // Abstraction function:
    //   AF(source, target, weight) =
    //     a directed edge from vertex `source` to vertex `target`
    //     with positive weight `weight`.
    //
    // Representation invariant:
    //   - source != null
    //   - target != null
    //   - weight > 0
    //
    // Safety from rep exposure:
    //   - All fields are private and final.
    //   - No setters; object is immutable.

    public Edge(String source, String target, int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("weight must be greater than zero");
        }
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }

    private void checkRep() {
        assert source != null;
        assert target != null;
        assert weight > 0;
    }

    public String getSource() { return source; }
    public String getTarget() { return target; }
    public int getWeight() { return weight; }

    @Override
    public String toString() {
        return source + "->" + target + " (" + weight + ")";
    }
}
