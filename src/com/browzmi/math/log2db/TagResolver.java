package com.browzmi.math.log2db;

import com.browzmi.math.entity.Url;
import com.browzmi.math.entity.Tag;
import com.browzmi.math.entity.Host;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 21.05.2008
 * Time: 0:52:19
 */
public final class TagResolver {
    private final EntityManager em;
    private final Map<String, Tag> cache;

    public TagResolver(EntityManager em) {
        this.em = em;
        this.cache = new TreeMap<String, Tag>();
    }

    public Tag get(String tag) {
        Tag entity = cache.get(tag);

        if (entity == null) {
            entity = Tag.find(em, tag);

            if (entity == null) {
                em.persist(entity = new Tag(tag));
            }

            cache.put(tag, entity);
        }

        return entity;
    }
}
