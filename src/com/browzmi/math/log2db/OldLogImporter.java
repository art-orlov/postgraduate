package com.browzmi.math.log2db;

import com.browzmi.math.entity.*;
import com.browzmi.math.Configuration;
import com.browzmi.tool.kit.Toolkit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

public final class OldLogImporter extends FileImport {
    public enum OldActionType {
        Comment, Post, Favorite, Friend, Rate
    }

/*
2007-12-04 06:36:44,469	action	1196768204469	6335	http://www.youtube.com/	Favorite
2007-12-04 06:36:44,469	tag	1196768204469	6335	http://www.youtube.com/	youtube
2007-12-04 06:36:44,469	action	1196768204469	6335	http://www.google.com/	Favorite
2007-12-04 06:36:44,469	tag	1196768204469	6335	http://www.google.com/	google
2007-12-04 06:36:44,485	action	1196768204485	6335	http://www.facebook.com/	Favorite
2007-12-04 06:36:44,485	tag	1196768204485	6335	http://www.facebook.com/	facebook
2007-12-04 06:36:44,485	action	1196768204485	6335	http://browzmi.blogspot.com/	Favorite
2007-12-04 06:36:44,485	tag	1196768204485	6335	http://browzmi.blogspot.com/	browzmi
2007-12-04 06:36:44,501	action	1196768204501	6335	http://www.browzmi.com/welcome/	Favorite
2007-12-04 06:36:44,501	tag	1196768204501	6335	http://www.browzmi.com/welcome/	browzmi
 */
    private static List<Url> restrictedFavoriteUrls = Arrays.asList(
		new Url(new Host("youtube.com", "www.youtube.com"), "/"),
		new Url(new Host("google.com", "www.google.com"), "/"),
		new Url(new Host("facebook.com", "www.facebook.com"), "/"),
		new Url(new Host("browzmi.blogspot.com", "browzmi.blogspot.com"), "/"),
		new Url(new Host("browzmi.com", "www.browzmi.com"), "/welcome/")
    );

    private final UrlResolver urlResolver;
    private final TagResolver tagResolver;
    private Action lastAction;

    public OldLogImporter(String fileName) throws IOException {
        this.urlResolver = new UrlResolver(em);
        this.tagResolver = new TagResolver(em);

        doImport(fileName);
    }

    protected String processLine(String line) {
        final String[] parts = line.split("\t");

        if (parts.length < 3) return "Empty line";
        
        //date  operation  utc  user_id  url  extra
        final String operation = parts[1];

        final String userId = Toolkit.getUserHash(Long.parseLong(parts[3]));
        final Date date = Toolkit.createDate(Long.parseLong(parts[2]));
        final Url url = urlResolver.get(parts[4]);

        if (operation.equals("view")) {
            em.persist(lastAction = new Browsing(userId, date, Action.OperationType.add, url, null));
        } else if (operation.equals("action")) {
            final OldActionType aType = OldActionType.valueOf(parts[5]);

            switch (aType) {
                case Friend:
                    //Wrong information in the log, id of a viewer in the url being viewed
                    break;
                case Favorite:
                    if (!restrictedFavoriteUrls.contains(url)) {
                        em.persist(lastAction = new Favorite(userId, date, Action.OperationType.add, url));
                    }
                    break;
                case Post:
                case Comment:
                    em.persist(lastAction = new Comment(userId, date, url));
                case Rate:
                    em.persist(lastAction = new Rate(userId, date, Action.OperationType.add, url, null));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } else if (operation.equals("tag")) {
            //игнорируем процедуру теггирования, если нет соответствующего фэйворита
            if (lastAction instanceof Favorite) {
                em.persist(new FavoriteTag((Favorite) lastAction, tagResolver.get(parts[5])));
            }
        }

        return date.toString();
    }

    public static void main(String[] args) throws IOException {
        new OldLogImporter("log 1 (old format).log");
    }
}
