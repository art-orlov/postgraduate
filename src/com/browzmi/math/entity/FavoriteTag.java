package com.browzmi.math.entity;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 21.05.2008
 * Time: 0:03:21
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "favorite_tag")
public class FavoriteTag {
    @SuppressWarnings({"UnusedDeclaration"})
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_id")
    private Favorite favorite;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public FavoriteTag() {
    }

    public FavoriteTag(Favorite favorite, Tag tag) {
        this.favorite = favorite;
        this.tag = tag;
    }

    public Favorite getFavorite() {
        return favorite;
    }

    public Tag getTag() {
        return tag;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final FavoriteTag that = (FavoriteTag) o;

        return favorite.equals(that.favorite) && tag.equals(that.tag);
    }

    public int hashCode() {
        return 31 * favorite.hashCode() + tag.hashCode();
    }
}
