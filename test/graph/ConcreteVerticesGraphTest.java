/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Map;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {

    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph();
    }

    /*
     * Testing ConcreteVerticesGraph.toString()
     *
     * Partition:
     * - empty graph
     * - graph with vertices but no edges
     * - graph with vertices and one edge
     * - graph with vertices and multiple edges
     */
    @Test
    public void testToStringEmptyGraph() {
        Graph<String> g = emptyInstance();
        String s = g.toString();
        assertTrue("string should mention vertices", s.toLowerCase().contains("vertices"));
    }

    @Test
    public void testToStringVerticesOnly() {
        Graph<String> g = emptyInstance();
        g.add("A");
        g.add("B");
        String s = g.toString();
        assertTrue(s.contains("A"));
        assertTrue(s.contains("B"));
    }

    @Test
    public void testToStringOneEdge() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 5);
        String s = g.toString();
        assertTrue(s.contains("A"));
        assertTrue(s.contains("B"));
        assertTrue(s.contains("5"));
    }

    @Test
    public void testToStringMultipleEdges() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 5);
        g.set("B", "C", 3);
        g.set("C", "A", 1);
        String s = g.toString();
        System.out.println(s);
        assertTrue(s.contains("A->{B=5}"));
        assertTrue(s.contains("B->{C=3}"));
        assertTrue(s.contains("C->{A=1}"));
    }

    /*
     * Testing Vertex
     *
     * Partition:
     * - constructor with valid name
     * - getName()
     * - setEdge() for adding, updating, and removing edges
     * - removeEdge()
     * - getTargets() defensive copy / immutability
     * - toString()
     */
    @Test
    public void testVertexConstructorAndGetName() {
        Vertex v = new Vertex("X");
        assertEquals("X", v.getName());
    }

    @Test
    public void testVertexSetEdgeAddAndUpdate() {
        Vertex v = new Vertex("X");
        int prev = v.setEdge("Y", 10);
        assertEquals(0, prev);
        Map<String, Integer> targets = v.getTargets();
        assertEquals(1, targets.size());
        assertEquals(Integer.valueOf(10), targets.get("Y"));

        // update existing edge
        int prev2 = v.setEdge("Y", 20);
        assertEquals(10, prev2);
        Map<String, Integer> targets2 = v.getTargets();
        assertEquals(Integer.valueOf(20), targets2.get("Y"));
    }

    @Test
    public void testVertexSetEdgeRemove() {
        Vertex v = new Vertex("X");
        v.setEdge("Y", 10);
        int prev = v.setEdge("Y", 0); // remove edge
        assertEquals(10, prev);
        Map<String, Integer> targets = v.getTargets();
        assertFalse(targets.containsKey("Y"));
    }

    @Test
    public void testVertexRemoveEdge() {
        Vertex v = new Vertex("X");
        v.setEdge("Y", 5);
        v.removeEdge("Y");
        Map<String, Integer> targets = v.getTargets();
        assertFalse(targets.containsKey("Y"));
    }

    @Test
    public void testVertexGetTargetsDefensiveCopy() {
        Vertex v = new Vertex("X");
        v.setEdge("Y", 10);
        Map<String, Integer> t = v.getTargets();
        try {
            t.put("Z", 5);
        } catch (UnsupportedOperationException e) {
            // expected
        }
        assertFalse(v.getTargets().containsKey("Z"));
    }

    @Test
    public void testVertexToString() {
        Vertex v = new Vertex("X");
        v.setEdge("Y", 10);
        String s = v.toString();
        assertTrue(s.contains("X"));
        assertTrue(s.contains("Y"));
        assertTrue(s.contains("10"));
    }
}