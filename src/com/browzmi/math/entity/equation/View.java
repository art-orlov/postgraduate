package com.browzmi.math.entity.equation;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

import com.browzmi.math.entity.Url;

/**
 * Date: 09.11.2008
 * Time: 15:40:18
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "view")
@org.hibernate.annotations.Table(appliesTo = "view")
public class View {
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

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "url_id")
	protected Url url;
}
