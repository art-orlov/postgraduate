package com.browzmi.math.entity;

import javax.persistence.*;
import java.net.URL;
import java.util.List;

import com.browzmi.math.log2db.HostResolver;
import com.browzmi.math.sql.SQL;
import org.hibernate.annotations.Index;

/*
* Project: Amigo4 (beta)
* Author: ArtemOrlov
* Created: 09.12.2007 17:33:49
* 
* Copyright (c) 1999-2007 Magenta Corporation Ltd. All Rights Reserved.
* Magenta Technology proprietary and confidential.
* Use is subject to license terms.
*/
@Entity
@Table(name = "url", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"host_id", "file_hash"})
})
@org.hibernate.annotations.Entity(mutable = false)
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "host_id")
    private Host host;

    @Basic
    @Column(name = "file", length = 3072, nullable = false, updatable = false)
    private String file;

    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
    @Basic
    @Column(name = "file_hash", nullable = false, updatable = false)
    private int fileHash;

    @Transient
    private final boolean shortHostName;

    public Url() {
        this.shortHostName = false;
    }

    public Url(UrlKey key, URL spec) {
        this.host = key.getHost();
        this.file = key.getFile();
        this.fileHash = key.getFile().hashCode();
        this.shortHostName = !spec.getHost().equals(key.getHost().getFullName());
    }

    public Url(Host host, String file) {
        this.host = host;
        this.file = file;
        this.fileHash = file.hashCode();
        this.shortHostName = false;
    }

    public final long getId() {
        return id;
    }

    public final Host getHost() {
        return host;
    }

    public final String getFile() {
        return file;
    }

    public boolean isShortHostName() {
        return shortHostName;
    }

    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Url url = (Url) o;

        return host.equals(url.host) && file.equals(url.file);
    }

    public final int hashCode() {
        return 31 * host.hashCode() + file.hashCode();
    }

    public String toString() {
        return "http://" + host.getFullName() + file;
    }

    public static Url find(EntityManager em, UrlKey key) {
        final List result = em.createNativeQuery(SQL.SELECT_URL, Url.class)
                .setParameter("HOST_ID", key.getHost().getId())
                .setParameter("FILE_HASH", key.getFile().hashCode())
                .setParameter("FILE", key.getFile())
                .setMaxResults(1)
                .getResultList();

        return result.size() > 0 ? (Url)result.get(0) : null;
    }

    //we ignore ref part here, and avoid calling URL.hashCode() because it uses dns lookup to resolve host name
    public static final class UrlKey implements Comparable<UrlKey> {
        private final Host host;
        private final String file;

        public UrlKey(Host host, String file) {
            this.host = host;
            this.file = file;
        }

        public Host getHost() {
            return host;
        }

        public String getFile() {
            return file;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final UrlKey urlKey = (UrlKey) o;

            return host.equals(urlKey.host) && file.equals(urlKey.file);
        }

        public int hashCode() {
            return 31 * host.hashCode() + file.hashCode();
        }

        public int compareTo(UrlKey o) {
            final int hostOrder = host.compareTo(o.host);
            return hostOrder == 0 ? file.compareTo(o.file) : hostOrder;
        }
    }
}
