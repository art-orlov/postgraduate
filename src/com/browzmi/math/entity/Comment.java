package com.browzmi.math.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 21.05.2008
 * Time: 0:27:04
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "comment")
public class Comment extends Action {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id")
    private Url url;

    public Comment() {
    }

    public Comment(String userId, Date date, Url url) {
        super(userId, date, OperationType.add);

        this.url = url;
    }

    public Url getUrl() {
        return url;
    }

    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
    public boolean equals(Object o) {
        return super.equals(o) && url.equals(((Comment) o).url);
    }
}
