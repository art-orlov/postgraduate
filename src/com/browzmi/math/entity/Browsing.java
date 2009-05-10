package com.browzmi.math.entity;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/*
* Project: Amigo4 (beta)
* Author: ArtemOrlov
* Created: 09.12.2007 21:21:09
* 
* Copyright (c) 1999-2007 Magenta Corporation Ltd. All Rights Reserved.
* Magenta Technology proprietary and confidential.
* Use is subject to license terms.
*/
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "browsing")
@org.hibernate.annotations.Table(appliesTo = "browsing", indexes = {
    @Index(name = "`user_id-url_id-operation-date`", columnNames = { "user_id", "url_id", "operation", "date" })        
})
public class Browsing extends Action {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id")
    private Url url;

    @Basic
    @Column(name = "window_id", nullable = true, updatable = false, columnDefinition = "char(22)")
    protected String windowId;

    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
    @Basic
    @Column(name = "short_host_name", nullable = false, updatable = false)
    private boolean shortHostName;

    public Browsing() {
    }

    public Browsing(String userId, Date date, OperationType operation, Url url, String windowId) {
        super(userId, date, operation);

        this.url = url;
        this.windowId = windowId;
        this.shortHostName = url.isShortHostName();
    }

    public final Url getUrl() {
        return url;
    }

    public String getWindowId() {
        return windowId;
    }

    public boolean equals(Object o) {
        if (o instanceof Browsing) {
            final Browsing b = (Browsing) o;

            return super.equals(o) && url.equals(b.url) && windowId.equals(b.windowId);
        } else {
            return false;
        }
    }
}
