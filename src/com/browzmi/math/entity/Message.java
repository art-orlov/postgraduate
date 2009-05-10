package com.browzmi.math.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 20.05.2008
 * Time: 0:09:07
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "message")
public class Message extends Action {
    @Basic
    @Column(name = "user_id2", nullable = false, updatable = false, columnDefinition = "char(22)")
    protected String userId2;

    public Message() {
    }

    public Message(String userId, Date date, String userId2) {
        super(userId, date, OperationType.add);

        this.userId2 = userId2;
    }

    public String getUserId2() {
        return userId2;
    }

    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
    public boolean equals(Object o) {
        return super.equals(o) && userId2.equals(((Friend)o).userId2);
    }
}
