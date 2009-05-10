package com.browzmi.math.calc.algorithm;

import junit.framework.TestCase;

import java.util.Set;
import java.util.List;

import gnu.trove.TLongHashSet;
import gnu.trove.THashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 30.12.2007
 * Time: 12:25:57
 */
public class TightlyCoupledGraph2Test extends TCGraphBaseTest {
    public abstract class Case extends BaseCase<THashSet<UID>, TightlyCoupledGraph2> {
        protected Class<TightlyCoupledGraph2> getGraphClass() {
            return TightlyCoupledGraph2.class;
        }
    }
    public void test() {
        new Case() {
            protected void create() {
                add(1, 2);
            }
            protected void check(THashSet<UID> result) {
                assertTrue(result.isEmpty());
            }
        };

        new Case() {
            protected void create() {
                add(1, 2);
                add(2, 3);
            }
            protected void check(THashSet<UID> result) {
                assertTrue(result.isEmpty());
            }
        };

        new Case() {
            protected void create() {
                add(1, 2);
                add(2, 3);
                add(3, 1);
            }
            protected void check(THashSet<UID> result) {
                assertEquals(3, result.size());
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
            protected void check(THashSet<UID> result) {
                assertEquals(3, result.size());
                assertTrue(result.contains(UID.of(1)));
                assertTrue(result.contains(UID.of(2)));
                assertTrue(result.contains(UID.of(3)));
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
            protected void check(THashSet<UID> result) {
                assertEquals(3, result.size());
                assertTrue(result.contains(UID.of(2)));
                assertTrue(result.contains(UID.of(3)));
                assertTrue(result.contains(UID.of(4)));
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
            protected void check(THashSet<UID> result) {
                assertEquals(4, result.size());
                assertTrue(result.contains(UID.of(1)));
                assertTrue(result.contains(UID.of(2)));
                assertTrue(result.contains(UID.of(3)));
                assertTrue(result.contains(UID.of(4)));
            }
        };
    }
}
