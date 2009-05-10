package com.browzmi.math.calc.algorithm;

import com.browzmi.math.entity.equation.Relation;
import com.browzmi.math.entity.equation.RKey;

import java.util.*;

import gnu.trove.*;
import gnu.trove.decorator.TObjectIntHashMapDecorator;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 30.12.2007
 * Time: 12:19:04
 */
public abstract class TCGraphBase<T> {
    protected final THashMap<UID, THashSet<UID>> nodes = new THashMap<UID, THashSet<UID>>();
    protected final List<UID> ordered;

    public TCGraphBase(final TObjectIntHashMap<Edge> edges) {
		@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
		final TObjectIntHashMapDecorator<Edge> dec = new TObjectIntHashMapDecorator<Edge>(edges);
		for (Edge edge : dec.keySet()) {
			addEdge(edge.getNode1(), edge.getNode2());
		}
        this.ordered = extractKeys(nodes);
    }

	private List<UID> extractKeys(THashMap<UID, THashSet<UID>> nodes) {
		return new ArrayList<UID>(nodes.keySet());
	}

	public TCGraphBase(final List<Relation> relations) {
        for (final Relation rel : relations) {
			final RKey key = rel.getKey();
			addEdge(UID.of(key.getUserId1()), UID.of(key.getUserId2()));
        }
        this.ordered = extractKeys(nodes);
    }

    private void addEdge(final UID node1, final UID node2) {
        if (!nodes.containsKey(node1)) {
            nodes.put(node1, new THashSet<UID>());
        }
        if (!nodes.containsKey(node2)) {
            nodes.put(node2, new THashSet<UID>());
        }
        nodes.get(node1).add(node2);
        nodes.get(node2).add(node1);
    }

    public final List<UID> getNodes() {
        return new ArrayList<UID>(ordered);
    }

    protected final boolean canAdd(THashSet<UID> subGraph, UID current) {
        final THashSet<UID> links = nodes.get(current);
        if (links.size() < subGraph.size()) return false;
		for (final UID uid : subGraph) {
			if (!links.contains(uid)) return false;
		}
        return true;
    }

    public abstract T process();
}
