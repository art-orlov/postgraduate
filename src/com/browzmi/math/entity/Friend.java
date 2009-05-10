package com.browzmi.math.entity;

import org.hibernate.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Basic;
import javax.persistence.Column;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 20.05.2008
 * Time: 0:08:59
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "friend")
@org.hibernate.annotations.Table(appliesTo = "friend", indexes = {
    @Index(name = "`user_id-user_id2`", columnNames = { "user_id", "user_id2" })        
})
public class Friend extends Action {
    @Basic
    @Column(name = "user_id2", nullable = false, updatable = false, columnDefinition = "char(22)")
    protected String userId2;

    public Friend() {
    }

    public Friend(String userId, Date date, OperationType operation, String userId2) {
        super(userId, date, operation);

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
