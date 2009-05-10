package com.browzmi.math.entity.equation;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 13.12.2007
 * Time: 0:56:33
 */
@Embeddable
public final class RKey implements Serializable {
    @Basic
    @Column(name = "step", updatable = false, nullable = false)
    private Date step;

    @Basic
	@Column(name = "user_id1", nullable = false, updatable = false, columnDefinition = "char(22)")
    private String userId1;

    @Basic
	@Column(name = "user_id2", nullable = false, updatable = false, columnDefinition = "char(22)")
    private String userId2;

    public Date getStep() {
        return step;
    }

    public String getUserId1() {
        return userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final RKey wKey = (RKey) o;

        return userId1.equals(wKey.userId1) && userId2.equals(wKey.userId2) && step.equals(wKey.step);
    }

	@Override
	public int hashCode() {
		int result = step.hashCode();
		result = 31 * result + userId1.hashCode();
		result = 31 * result + userId2.hashCode();
		return result;
	}
}
