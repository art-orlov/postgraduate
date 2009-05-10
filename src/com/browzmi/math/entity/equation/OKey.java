package com.browzmi.math.entity.equation;

import javax.persistence.Embeddable;
import javax.persistence.Basic;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 17.12.2007
 * Time: 0:43:31
 */
@Embeddable
public final class OKey implements Serializable {
    @Basic
    @Column(name = "step", updatable = false, nullable = false)
    private Date step;

    @Basic
	@Column(name = "user_id", nullable = false, updatable = false, columnDefinition = "char(22)")
    private String userId;

    @Basic
    @Column(name = "url_id", updatable = false, nullable = false)
    private long urlId;

    public OKey() {
    }

    public Date getStep() {
        return step;
    }

    public String getUserId() {
        return userId;
    }

    public long getUrlId() {
        return urlId;
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final OKey vKey = (OKey) o;

        return urlId == vKey.urlId && userId.equals(vKey.userId) && step.equals(vKey.step);
    }

	@Override
	public int hashCode() {
		int result = step.hashCode();
		result = 31 * result + userId.hashCode();
		result = 31 * result + (int) (urlId ^ (urlId >>> 32));
		return result;
	}
}
