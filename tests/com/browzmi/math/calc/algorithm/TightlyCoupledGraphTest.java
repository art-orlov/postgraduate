package com.browzmi.math.calc.algorithm;

import junit.framework.TestCase;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.TreeMap;

import gnu.trove.TLongHashSet;
import gnu.trove.THashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 26.12.2007
 * Time: 23:18:55
 */
public class TightlyCoupledGraphTest extends TCGraphBaseTest {
    public abstract class Case extends BaseCase<List<THashSet<UID>>, TightlyCoupledGraph> {
        protected Class<TightlyCoupledGraph> getGraphClass() {
            return TightlyCoupledGraph.class;
        }
    }

    public void test() {
        new Case() {
            protected void create() {
                add(1, 2);
            }
            protected void check(List<THashSet<UID>> result) {
                assertTrue(result.isEmpty());
            }
        };

        new Case() {
            protected void create() {
                add(1, 2);
                add(2, 3);
            }
            protected void check(List<THashSet<UID>> result) {
                assertTrue(result.isEmpty());
            }
        };

        new Case() {
            protected void create() {
                add(1, 2);
                add(2, 3);
                add(3, 1);
            }
            protected void check(List<THashSet<UID>> result) {
                assertEquals(1, result.size());
                assertEquals(3, result.get(0).size());
                assertTrue(result.get(0).contains(UID.of(1)));
                assertTrue(result.get(0).contains(UID.of(2)));
                assertTrue(result.get(0).contains(UID.of(3)));
            }
        };

        new Case() {
            protected void create() {
                add(1, 2);
                add(2, 3);
                add(3, 1);
                add(2, 4);
                add(4, 5);
                add(5, 2);
            }
            protected void check(List<THashSet<UID>> result) {
                assertEquals(2, result.size());
                assertEquals(3, result.get(0).size());
                assertEquals(3, result.get(1).size());
                assertTrue(result.get(0).contains(UID.of(1)));
                assertTrue(result.get(0).contains(UID.of(2)));
                assertTrue(result.get(0).contains(UID.of(3)));
                assertTrue(result.get(1).contains(UID.of(2)));
                assertTrue(result.get(1).contains(UID.of(4)));
                assertTrue(result.get(1).contains(UID.of(5)));
            }
        };

        new Case() {
            protected void create() {
                add(1, 2);
                add(2, 3);
                add(3, 4);
                add(2, 4);
                add(3, 5);
                add(4, 6);
            }
            protected void check(List<THashSet<UID>> result) {
                assertEquals(1, result.size());
                assertEquals(3, result.get(0).size());
                assertTrue(result.get(0).contains(UID.of(2)));
                assertTrue(result.get(0).contains(UID.of(3)));
                assertTrue(result.get(0).contains(UID.of(4)));
            }
        };

        new Case() {
            protected void create() {
                add(1, 2);
                add(2, 3);
                add(3, 4);
                add(1, 4);
                add(1, 3);
                add(2, 4);
                add(2, 5);
                add(3, 5);
                add(4, 5);
                add(6, 7);
                add(7, 8);
                add(6, 8);
            }
            protected void check(List<THashSet<UID>> result) {
                assertEquals(3, result.size());
				assertEquals(3, result.get(0).size());
				assertEquals(4, result.get(1).size());
				assertEquals(4, result.get(2).size());
				assertTrue(result.get(1).contains(UID.of(1)));
                assertTrue(result.get(1).contains(UID.of(2)));
                assertTrue(result.get(1).contains(UID.of(3)));
                assertTrue(result.get(1).contains(UID.of(4)));
                assertTrue(result.get(2).contains(UID.of(2)));
                assertTrue(result.get(2).contains(UID.of(3)));
                assertTrue(result.get(2).contains(UID.of(4)));
                assertTrue(result.get(2).contains(UID.of(5)));
            }
        };
    }
}
