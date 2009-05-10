package com.browzmi.math.calc.algorithm;

/**
 * Created by IntelliJ IDEA.
* User: Artem
* Date: 26.12.2007
* Time: 23:14:53
*/
public final class Edge implements Comparable<Edge> {
    private final UID node1;
    private final UID node2;
	private final int hashCode;

    public Edge(UID node1, UID node2) {
        this.node1 = node1;
        this.node2 = node2;

		this.hashCode = computeHashCode(node1, node2);
    }

    public UID getNode1() {
        return node1;
    }

    public UID getNode2() {
        return node2;
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Edge edge = (Edge) o;

        return hashCode == edge.hashCode && node1.equals(edge.node1) && node2.equals(edge.node2);
    }

	@Override
	public int hashCode() {
		return hashCode;
	}

	private static int computeHashCode(UID node1, UID node2) {
        return 31 * node1.hashCode() + node2.hashCode();
    }

    public int compareTo(Edge o) {
		final int cmp1 = node1.compareTo(o.node1);
        return cmp1 == 0 ? node2.compareTo(o.node2) : cmp1;
    }
}
