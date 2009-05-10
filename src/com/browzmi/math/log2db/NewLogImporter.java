package com.browzmi.math.log2db;

import com.browzmi.tool.kit.Toolkit;
import com.browzmi.math.entity.*;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 20.05.2008
 * Time: 23:58:02
 */
public class NewLogImporter extends FileImport {
    private final UrlResolver urlResolver;
    private final TagResolver tagResolver;

//	private static final long startDatetime = new GregorianCalendar(2008, 10, 1, 10, 0, 0).getTimeInMillis();
	private static long offset = 0;

    public NewLogImporter(String fileName) throws IOException {
        this.urlResolver = new UrlResolver(em);
        this.tagResolver = new TagResolver(em);

        doImport(fileName);
    }

    protected String processLine(String line) {
        final String[] parts = line.split("\t");

        if (parts.length < 3) return "Empty line";

        //date  timestamp  action_type  operation_type  user_id  extra_1  extra_2
        final Date date = extractDate(parts[1]);
        final ActionType aType = ActionType.valueOf(parts[2]);
        final Action.OperationType oType = Action.OperationType.valueOf(parts[3].toLowerCase());
        final String userId = processUserId(parts[4]);
        final String extra1 = parts[5];
        final String extra2 = parts.length > 6 ? parts[6] : null;

        switch (aType) {
            case comment:
                em.persist(new Comment(userId, date, urlResolver.get(extra1)));
                break;
            case favorite:
                final Favorite fav = new Favorite(userId, date, oType, urlResolver.get(extra1));
                em.persist(fav);
                if (oType != Action.OperationType.remove) {
                    persistFavoriteTags(fav, extra2);
                }
                break;
            case friend:
                final String userId2 = processUserId(extra1.length() < 22 ? Toolkit.getUserHash(Long.parseLong(extra1)) : extra1);
                em.persist(new Friend(userId, date, oType, userId2));
                break;
            case location:
                em.persist(new Browsing(userId, date, oType, urlResolver.get(extra1), extra2));
                break;
            case rate:
                em.persist(new Rate(userId, date, oType, urlResolver.get(extra1), Byte.valueOf(extra2)));
                break;
            case clip:
                em.persist(new Clip(userId, date, oType, urlResolver.get(extra1), urlResolver.get(extra2)));
                break;
        }

        return date.toString();
    }

	private static Date extractDate(String dateStr) {
		long time = Long.parseLong(dateStr);
//		if (offset == 0) {
//			offset = startDatetime - time;
//		}
		return Toolkit.createDate(time + offset);
	}

	private static String processUserId(String uid) {
		return uid;//'4' + uid.substring(1);
	}

	private void persistFavoriteTags(Favorite fav, String s) {
        try {
            final JSONArray tags = new JSONArray(s);
            for (int i = 0; i < tags.length(); i++) {
                em.persist(new FavoriteTag(fav, tagResolver.get(tags.getString(i))));                
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

	private static enum ActionType {
        location, favorite, friend, rate, comment, clip
    }

    public static void main(String[] args) throws IOException {
        new NewLogImporter("07 -.log");
    }
}
