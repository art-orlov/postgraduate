package com.browzmi.math.entity.equation;

import javax.persistence.MappedSuperclass;
import javax.persistence.EmbeddedId;
import javax.persistence.Basic;
import javax.persistence.Column;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 22.12.2007
 * Time: 23:02:37
 */
@MappedSuperclass
public class Operation {
    @EmbeddedId
    private OKey key;

    @Basic
    @Column(name = "value", nullable = false, updatable = false)
    private int value;

    public OKey getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operation)) return false;

        final Operation that = (Operation) o;

        return key.equals(that.key);

    }

    public int hashCode() {
        return key.hashCode();
    }
}
