package com.browzmi.math.entity;

import com.browzmi.math.sql.SQL;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 04.01.2008
 * Time: 0:30:32
 */
@Entity
@Table(name = "host")
public class Host implements Comparable<Host> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @Basic
    @Column(name = "norm_name", length = 255, nullable = false, updatable = false, unique = true)
    private String normName;

    @Basic
    @Column(name = "full_name", length = 255, nullable = false, updatable = true)
    private String fullName;

    public Host() {
    }

    public Host(String normName, String fullName) {
        this.normName = normName;
        this.fullName = fullName;
    }

    public final long getId() {
        return id;
    }

    public String getNormName() {
        return normName;
    }

    public String getFullName() {
        return fullName;
    }

    public void update(String name) {
        if (!normalize(name).equals(normName)) {
            throw new IllegalArgumentException();
        }
        if (fullName.length() < name.length()) {
            fullName = name;
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Host host = (Host) o;

        return normName.equals(host.normName);
    }

    public int hashCode() {
        return normName.hashCode();
    }

    public int compareTo(Host o) {
        return normName.compareTo(o.normName);
    }

    public static String normalize(String host) {
        return host.startsWith("www.") && host.indexOf(".", 4) != -1 ? host.substring(4) : host;
    }

    public static Host find(EntityManager em, String normName) {
        final List result = em
                .createNativeQuery(SQL.SELECT_HOST, Host.class)
                .setParameter("NORM_NAME", normName)
                .setMaxResults(1)
                .getResultList();
        return result.size() > 0 ? (Host)result.get(0) : null;
    }
}
