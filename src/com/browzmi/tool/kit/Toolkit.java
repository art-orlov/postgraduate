package com.browzmi.tool.kit;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 20.05.2008
 * Time: 23:52:17
 */
public class Toolkit {
    private final Digest digest;
    private static final long OPTIMAL_INTERSECTION = 12 * 60 * 60 * 1000;

    private Toolkit() {
        digest = new Digest();
    }

    private static class LazyHolder {
        static final Toolkit instance = new Toolkit();
    }

    private static Toolkit getInstance() {
        return LazyHolder.instance;
    }

    public static Digest getDigest() {
        return getInstance().digest;
    }

    public static String getUserHash(long userId) {
        return Secure.stringify(getDigest().getHashCode(Long.toString(userId)));
    }

    public static Date createDate(long time) {
        return new Date(time/* - OPTIMAL_INTERSECTION*/);
    }

    public static void main(String[] args) {
        //Browzmi System User   => MMTQE2Ku7HJKBO6TKJenUC
        //Travis                => 3macPOgUabR2ZBjWafWWls
        System.out.println(getUserHash(4));
    }
}
