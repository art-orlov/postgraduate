package com.browzmi.math.calc.algorithm;

import com.browzmi.math.entity.equation.Relation;

import java.util.*;

import gnu.trove.TLongHashSet;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.THashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 30.12.2007
 * Time: 12:14:26
 */
public final class TightlyCoupledGraph2 extends TCGraphBase<THashSet<UID>> {
    private final THashSet<UID> result = new THashSet<UID>();

    public TightlyCoupledGraph2(TObjectIntHashMap<Edge> edges) {
        super(edges);
    }

    public TightlyCoupledGraph2(List<Relation> relations) {
        super(relations);
    }

    public THashSet<UID> process() {
        result.clear();

        find(new THashSet<UID>(), 0);

        return result;
    }

    private boolean find(THashSet<UID> subGraph, int index) {
        boolean found = false;
        for (int i = index; i < ordered.size(); i++) {
            final UID current = ordered.get(i);
            if (canAdd(subGraph, current) && (subGraph.size() + ordered.size() - i > result.size())) {
                subGraph.add(current);
                found = find(subGraph, i + 1) || found;
                subGraph.remove(current);
            }
        }
        if (!found) {
            if (subGraph.size() >= 3 && subGraph.size() > result.size()) {
                result.clear();
                result.addAll(subGraph);
                return true;
            }
        }
        return found;
    }
}
