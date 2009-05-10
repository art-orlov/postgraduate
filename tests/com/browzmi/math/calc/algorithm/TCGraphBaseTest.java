package com.browzmi.math.calc.algorithm;

import junit.framework.TestCase;

import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

import gnu.trove.TObjectIntHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 30.12.2007
 * Time: 12:26:37
 */
public class TCGraphBaseTest extends TestCase {
    public abstract static class BaseCase<T, G extends TCGraphBase<T>> {
        protected final TObjectIntHashMap<Edge> edges = new TObjectIntHashMap<Edge>();

        public BaseCase() {
            create();
            try {
                check(getGraphClass().getConstructor(TObjectIntHashMap.class).newInstance(edges).process());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        protected abstract void create();

        protected abstract Class<G> getGraphClass();

        protected abstract void check(T result);

        protected void add(int node1, int node2) {
            assertTrue(node1 != node2);
			final UID u1 = UID.of(Math.min(node1, node2));
			final UID u2 = UID.of(Math.max(node1, node2));
            edges.put(new Edge(u1, u2), 1);
        }
    }
}
