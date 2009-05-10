package com.browzmi.math.entity.equation;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 22.12.2007
 * Time: 23:07:05
 */
@MappedSuperclass
public class Relation {
    @EmbeddedId
    private RKey key;

    @Basic
    @Column(name = "value", nullable = false, updatable = false)
    private int value;

    public RKey getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Relation w = (Relation) o;

        return key.equals(w.key);
    }

    public int hashCode() {
        return key.hashCode();
    }
}
