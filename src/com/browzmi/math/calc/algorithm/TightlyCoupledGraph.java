package com.browzmi.math.calc.algorithm;

import com.browzmi.math.entity.equation.Relation;

import java.util.*;

import gnu.trove.TLongHashSet;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.TLongIterator;
import gnu.trove.THashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 25.12.2007
 * Time: 0:30:00
 */
public final class TightlyCoupledGraph extends TCGraphBase<List<THashSet<UID>>> {
    private final List<THashSet<UID>> result = new ArrayList<THashSet<UID>>();

    public TightlyCoupledGraph(TObjectIntHashMap<Edge> edges) {
        super(edges);
    }

    public TightlyCoupledGraph(List<Relation> relations) {
        super(relations);
    }

    public List<THashSet<UID>> process() {
        result.clear();

        find(new THashSet<UID>(), 0);

        return result;
    }

    private boolean find(THashSet<UID> subGraph, int index) {
        boolean found = false;
        for (int i = index; i < ordered.size(); i++) {
            final UID current = ordered.get(i);
            if (canAdd(subGraph, current)) {
                subGraph.add(current);
                found = find(subGraph, i + 1) || found;
                subGraph.remove(current);
            }
        }
        if (!found) { //clique found
            if (subGraph.size() >= 3) {
                tryAdd(subGraph);
                return true;
            }
        }
        return found;
    }

    private void tryAdd(final THashSet<UID> subGraph) {
        for (final THashSet<UID> found : result) {
            boolean contains = true;
			for (final UID uid : subGraph) {
				if (!found.contains(uid)) {
					contains = false;
					break;
				}
			}
            if (contains) return;
        }
        result.add((THashSet<UID>) subGraph.clone());
    }
}
