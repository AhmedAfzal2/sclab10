/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }

    /*
     * Testing ConcreteEdgesGraph.toString()
     *
     * Partition:
     *  - empty graph
     *  - graph with vertices but no edges
     *  - graph with vertices and edges (1 edge)
     *  - graph with multiple edges
     */

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testToStringEmpty() {
        Graph<String> g = emptyInstance();
        String s = g.toString().toLowerCase();
        assertTrue("string should mention no vertices or edges",
                s.contains("vertices") || s.contains("empty"));
    }

    @Test
    public void testToStringVerticesOnly() {
        Graph<String> g = emptyInstance();
        g.add("A");
        g.add("B");
        String s = g.toString();

        assertTrue(s.contains("A"));
        assertTrue(s.contains("B"));
        assertFalse(s.contains("A->B"));   // no edges
    }

    @Test
    public void testToStringOneEdge() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 5);

        String s = g.toString();

        assertTrue(s.contains("A"));
        assertTrue(s.contains("B"));
        assertTrue(s.contains("A->B"));
        assertTrue(s.contains("5"));
    }

    @Test
    public void testToStringMultipleEdges() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 5);
        g.set("B", "C", 3);
        g.set("C", "A", 1);

        String s = g.toString();

        assertTrue(s.contains("A->B"));
        assertTrue(s.contains("B->C"));
        assertTrue(s.contains("C->A"));
    }

    /*
     * Testing Edge
     *
     * Partitions:
     *  - constructor: normal (positive weight)
     *  - getters: source, target, weight
     *  - toString contains useful information
     */

    @Test
    public void testEdgeConstructorAndGetters() {
        Edge e = new Edge("A", "B", 10);

        assertEquals("A", e.getSource());
        assertEquals("B", e.getTarget());
        assertEquals(10, e.getWeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEdgeConstructorZeroWeight() {
        new Edge("A", "B", 0);
    }

    @Test
    public void testEdgeToString() {
        Edge e = new Edge("A", "B", 10);
        String s = e.toString();

        assertTrue(s.contains("A"));
        assertTrue(s.contains("B"));
        assertTrue(s.contains("10"));
    }
}
