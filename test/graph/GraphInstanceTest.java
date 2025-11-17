/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    @Test
    public void testAddVertex() {
        Graph<String> g = emptyInstance();
        g.add("A");
        assertEquals(Set.of("A"), g.vertices());
        g.add("B");
        assertEquals(Set.of("A","B"), g.vertices());
    }

    @Test
    public void testAddDuplicateVertex() {
        Graph<String> g = emptyInstance();
        g.add("A");
        boolean addedAgain = g.add("A");
        assertFalse("adding duplicate vertex should return false", addedAgain);
        assertEquals(Set.of("A"), g.vertices());
    }

    // also tests getting target and sources of edge
    @Test
    public void testAddEdge() {
        Graph<String> g = emptyInstance();
        g.add("A");
        g.add("B");
        g.set("A","B",3);
        assertEquals(Map.of("B",3), g.targets("A"));
        assertEquals(Map.of("A",3), g.sources("B"));
    }

    @Test
    public void testUpdateEdgeWeight() {
        Graph<String> g = emptyInstance();
        g.add("A");
        g.add("B");
        g.set("A","B",3);
        g.set("A","B",5);
        assertEquals(Map.of("B",5), g.targets("A"));
        assertEquals(Map.of("A",5), g.sources("B"));
    }

    @Test
    public void testRemoveEdge() {
        Graph<String> g = emptyInstance();
        g.add("A");
        g.add("B");
        g.set("A","B",3);
        // the specification of set() says it will remove the edge if weight is 0
        g.set("A","B",0);
        assertTrue(g.targets("A").isEmpty());
        assertTrue(g.sources("B").isEmpty());
    }

    @Test
    public void testRemoveVertex() {
        Graph<String> g = emptyInstance();
        g.add("A");
        g.add("B");
        g.set("A","B",3);
        g.remove("B");
        assertFalse(g.vertices().contains("B"));
        assertTrue(g.targets("A").isEmpty());
        assertTrue(g.sources("B").isEmpty());
    }

    @Test
    public void testVerticesListImmutable() {
        Graph<String> g = emptyInstance();
        g.add("A");
        Set<String> vs = g.vertices();
        try {
            vs.add("B");
            assertEquals("B should not be added to the actual graph from vertices set", Set.of("A"), g.vertices());
        } catch (UnsupportedOperationException e) {
            // intended behavior
        }
        assertFalse(g.vertices().contains("B"));
    }

}
