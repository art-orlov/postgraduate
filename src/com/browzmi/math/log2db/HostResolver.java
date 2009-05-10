package com.browzmi.math.log2db;

import com.browzmi.math.entity.Host;
import com.browzmi.math.sql.SQL;

import javax.persistence.EntityManager;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 04.01.2008
 * Time: 0:43:34
 */
public final class HostResolver {
    private final EntityManager em;
    private final Map<String, Host> cache;

    public HostResolver(EntityManager em) {
        this.em = em;
        this.cache = new TreeMap<String, Host>(); 
    }

    public Host get(String host) {
        final String key = Host.normalize(host);
        Host entity = cache.get(key);

        if (entity == null) {
            entity = Host.find(em, key);

            if (entity == null) {
                em.persist(entity = new Host(key, host));
            }

            cache.put(key, entity);
        }

        if (entity.getId() > 0) {
            entity.update(host);
        }

        return entity;
    }
}
