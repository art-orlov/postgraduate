package com.browzmi.math.log2db;

import com.browzmi.math.entity.*;
import com.browzmi.math.sql.SQL;
import com.browzmi.tool.kit.Toolkit;

import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

public class FriendshipImport extends FileImport {
//    private final Date cutOff;

    public FriendshipImport(String fileName) throws IOException {
//        final Calendar t = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        //обрезание по 30.04
//        t.set(2008, 5, 1);
//        this.cutOff = t.getTime();

        et = em.getTransaction();

        et.begin();
        em.createNativeQuery("delete from friend").executeUpdate();
        et.commit();
        et = null;

        doImport(fileName);
    }

    protected String processLine(String line) {
        final String[] parts = line.split("\t");

        if (parts.length < 3) return "Empty line";

        //date  user_id1  user_id2
        final Date date = Toolkit.createDate(1000 * Long.parseLong(parts[0]));
//        if (date.after(cutOff)) {
//            return "";
//        }

        final String userId1 = Toolkit.getUserHash(Long.parseLong(parts[1]));
        final String userId2 = Toolkit.getUserHash(Long.parseLong(parts[2]));

        em.persist(new Friend(userId1, date, Action.OperationType.add, userId2));
        em.persist(new Friend(userId2, date, Action.OperationType.add, userId1));

        return date.toString();
    }

    public static void main(String[] args) throws IOException {
        new FriendshipImport("friendship.txt");
    }
}
