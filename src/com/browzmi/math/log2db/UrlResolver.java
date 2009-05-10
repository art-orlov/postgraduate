package com.browzmi.math.log2db;

import com.browzmi.math.entity.Url;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 04.01.2008
 * Time: 1:09:20
 */
public final class UrlResolver {
    private final EntityManager em;
    private final HostResolver hostResolver;
    private final Map<Url.UrlKey, Url> cache;

    public UrlResolver(EntityManager em) {
        this.em = em;
        this.hostResolver = new HostResolver(em);
        this.cache = new TreeMap<Url.UrlKey, Url>();
    }

    public Url get(String spec) {
        final URL fSpec = filterUrl(spec);
        final Url.UrlKey key = generateKey(fSpec);
        Url entity = cache.get(key);

        if (entity == null) {
            entity = Url.find(em, key);

            if (entity == null) {
                em.persist(entity = new Url(key, fSpec));
            }

            cache.put(key, entity);
        }

        return entity;
    }

    private Url.UrlKey generateKey(URL url) {
        return new Url.UrlKey(hostResolver.get(url.getHost()), url.getFile());
    }

    //А здесь мы делаем все юрлы http:// и удаляем ref часть
    public URL filterUrl(String spec) {
        try {
            final URL raw = new URL(spec);
            return new URL("http", raw.getHost().toLowerCase(), raw.getFile());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
