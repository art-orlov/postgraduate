package com.browzmi.math.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Date: 19.10.2008
 * Time: 19:54:45
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "clip")
public class Clip extends Action {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id")
    private Url url;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "img_url_id")
    private Url imgUrl;

    @Deprecated
    public Clip() {
    }

    public Clip(String userId, Date date, OperationType operation, Url url, Url imgUrl) {
        super(userId, date, operation);
        
        this.url = url;
        this.imgUrl = imgUrl;
    }

    public Url getUrl() {
        return url;
    }

    public Url getImgUrl() {
        return imgUrl;
    }

    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
    public boolean equals(Object o) {
        return super.equals(o) && url.equals(((Clip) o).url) && imgUrl.equals(((Clip) o).imgUrl);
    }
}
