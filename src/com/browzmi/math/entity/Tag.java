package com.browzmi.math.entity;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.List;

import com.browzmi.math.sql.SQL;

/*
* Project: Amigo4 (beta)
* Author: ArtemOrlov
* Created: 10.12.2007 12:18:39
* 
* Copyright (c) 1999-2007 Magenta Corporation Ltd. All Rights Reserved.
* Magenta Technology proprietary and confidential.
* Use is subject to license terms.
*/
@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @Basic
    @Column(name = "name", length = 255, nullable = false, updatable = false, unique = true)
    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public final long getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && name.equals(((Tag) o).name);

    }

    public final int hashCode() {
        return name.hashCode();
    }

    public static Tag find(EntityManager em, String name) {
        final List result = em.createNativeQuery(SQL.SELECT_TAG, Tag.class)
                .setParameter("NAME", name)
                .setMaxResults(1)
                .getResultList();
        return result.size() > 0 ? (Tag)result.get(0) : null;
    }
}
