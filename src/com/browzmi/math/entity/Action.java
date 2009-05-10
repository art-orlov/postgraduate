package com.browzmi.math.entity;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/*
* Project: Amigo4 (beta)
* Author: ArtemOrlov
* Created: 09.12.2007 17:42:56
* 
* Copyright (c) 1999-2007 Magenta Corporation Ltd. All Rights Reserved.
* Magenta Technology proprietary and confidential.
* Use is subject to license terms.
*/
@MappedSuperclass
public abstract class Action {
    public enum OperationType {
        add, remove, update
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    protected long id;

    @Basic
    @Column(name = "user_id", nullable = false, updatable = false, columnDefinition = "char(22)")
    @Index(name = "`user_id`")
    protected String userId;

    @Basic
    @Column(name = "date", nullable = false, updatable = false)
    protected Date date;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "operation", columnDefinition = "enum ('add', 'update', 'remove')", nullable = false, updatable = false)
    protected OperationType operation;

    protected Action() {
    }

    protected Action(String userId, Date date, OperationType operation) {
        this.userId = userId;
        this.date = date;
        this.operation = operation;
    }

    public final long getId() {
        return id;
    }

    public final String getUserId() {
        return userId;
    }

    public final Date getDate() {
        return date;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Action action = (Action) o;

        return userId.equals(action.userId) && date.equals(action.date) && operation.equals(operation);
    }

    public final int hashCode() {
        return 31 * userId.hashCode() + date.hashCode();
    }
}
