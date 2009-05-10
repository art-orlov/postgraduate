package com.browzmi.math.entity;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 21.05.2008
 * Time: 0:29:36
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "rate")
@org.hibernate.annotations.Table(appliesTo = "rate", indexes = {
    @Index(name = "`user_id-url_id-date`", columnNames = { "user_id", "url_id", "date" })
})
public class Rate extends Action {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id")
    private Url url;

    @Basic
    @Column(name = "value", nullable = true, updatable = false)
    private Byte value;

    public Rate() {
    }

    public Rate(String userId, Date date, OperationType operation, Url url, Byte value) {
        super(userId, date, operation);
        this.url = url;
        this.value = value;
    }

    public Url getUrl() {
        return url;
    }

    public Byte getValue() {
        return value;
    }

    public boolean equals(Object o) {
        if (o instanceof Rate) {
            final Rate rate = (Rate) o;

            return super.equals(o) && url.equals(rate.url) && (value == null ? rate.value == null : value.equals(rate.value));

        } else {
            return false;
        }
    }
}
