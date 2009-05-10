package com.browzmi.util;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 25.12.2007
 * Time: 0:43:57
 */
public final class Pair<A, B> implements Serializable, Cloneable {
    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<A, B>(first, second);
    }

    public final A first;
    public final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public final A getFirst() {
        return first;
    }

    public final B getSecond() {
        return second;
    }

    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException"})
    public final Pair<A, B> clone() {
        try {
            //noinspection unchecked
            return (Pair<A, B>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object object) {
        if (object instanceof Pair<?, ?>) {
            final Pair<?, ?> other = (Pair<?, ?>) object;
            return equals(first, other.first) && equals(second, other.second);
        }
        return false;
    }

    public final int hashCode() {
        return hash(first, 0) ^ hash(second, 0);
    }

    public final String toString() {
        return String.format("(%s, %s)", first, second);
    }

    private static boolean equals(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    private static int hash(Object object, int nullHash) {
        return (object == null) ? nullHash : object.hashCode();
    }
}