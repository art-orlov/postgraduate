package com.browzmi.math.entity;

import javax.persistence.*;
import java.util.Date;

/*
* Project: Amigo4 (beta)
* Author: ArtemOrlov
* Created: 09.12.2007 17:46:59
* 
* Copyright (c) 1999-2007 Magenta Corporation Ltd. All Rights Reserved.
* Magenta Technology proprietary and confidential.
* Use is subject to license terms.
*/
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "favorite")
public class Favorite extends Action {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id")
    private Url url;
    
    public Favorite() {
    }

    public Favorite(String userId, Date date, OperationType operation, Url url) {
        super(userId, date, operation);

        this.url = url;
    }

    public final Url getUrl() {
        return url;
    }

    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
    public boolean equals(Object o) {
        return super.equals(o) && url.equals(((Favorite) o).url);
    }
}
