package com.browzmi.math.model;

import com.browzmi.math.entity.*;
import com.browzmi.math.Configuration;
import com.browzmi.tool.kit.Toolkit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.*;

public final class Generator {
    private final Random rnd;
    private final EntityManager em;
    private final Host host;
    private int userGuid;
    private int urlNumber;

    public static final long DAY = 24 * 60 * 60 * 1000;

    public static final Date EPOCH_START = new GregorianCalendar(2008, 3, 1).getTime();
    public static final int MODELLING_PERIOD = 44;

    public static final int USERS = 24;
    public static final double STAR_USERS = 0.05;
    public static final double CORE_USERS = 0.45;
    public static final double CORE_ACTIVE_DAY = 0.6; // 0.7 = 5 / 7
    //public static final double CORE_ACTIVE_DAY = 0.84; // 0.7 = 5 / 7  => 0.6 => 4.2 / 5 = 0.84
    public static final double NEWCOMER_LIFE_DEVIATION = 2.5;
    public static final double FRIEND_DEGREE = 3.4;
    public static final double NEWCOMER_CAN_HAVE_FRIENDS = 0.4;
    public static final double NEWCOMER_WF_CAN_RECOMMEND = 0.5;
    public static final double NEWCOMER_WOF_CAN_RECOMMEND = 0.2;
    public static final double NEWCOMER_RECOMMEND_JUST_TO_TRY = 0.7;

    public static final double STAR_NEW_PAGES_PER_DAY_BASE = 120;
    public static final double STAR_NEW_PAGES_PER_DAY_DEVIATION = 10;
    public static final double CORE_NEW_PAGES_PER_DAY_BASE = 17;
    public static final double CORE_NEW_PAGES_PER_DAY_DEVIATION = 5;
    public static final double NEWCOMER_NEW_PAGES_PER_DAY_BASE = 1;
    public static final double NEWCOMER_NEW_PAGES_PER_DAY_DEVIATION = 10;

    public static final double STAR_RECOMMEND = 1.0 / 24;
    public static final double STAR_RECOMMEND_DEVIATION = 1;
    public static final double CORE_RECOMMEND = 1.0 / 8;
    public static final double CORE_RECOMMEND_DEVIATION = 0.5;
    public static final double NEWCOMER_RECOMMEND = 1.0 / 6;
    public static final double NEWCOMER_RECOMMEND_DEVIATION = 0.5;

    public static final double STAR_FOLLOW_RECOMENDATION = 1.0 / 6;
    public static final double STAR_FOLLOW_RECOMENDATION_DEVIATION = 0.5;
    public static final double CORE_FOLLOW_RECOMENDATION = 1.0 / 4;
    public static final double CORE_FOLLOW_RECOMENDATION_DEVIATION = 0.5;
    public static final double NEWCOMER_FOLLOW_RECOMENDATION = 1.0 / 3;
    public static final double NEWCOMER_FOLLOW_RECOMENDATION_DEVIATION = 0.5;

    public static final double FOLLOW_PUBLIC_RECOMENDATION = 0.5;
    public static final double FOLLOW_PUBLIC_RECOMENDATION_DEVIATION = 1;

    public Generator() {
        this.rnd = new Random();
        this.em = Configuration.getInstance().em();
        this.host = new Host("model.browzmi.com", "www.model.browzmi.com");
        this.userGuid = 1;
        this.urlNumber = 1;
    }

    public void game() {
        EntityTransaction et = null;
        try {
            et = em.getTransaction();

            et.begin();
            em.createNativeQuery("drop table if exists action").executeUpdate();
            em.createNativeQuery("delete from rate").executeUpdate();
            em.createNativeQuery("delete from comment").executeUpdate();
            em.createNativeQuery("delete from favorite_tag").executeUpdate();
            em.createNativeQuery("delete from favorite").executeUpdate();
            em.createNativeQuery("delete from browsing").executeUpdate();
            em.createNativeQuery("delete from url").executeUpdate();
            em.createNativeQuery("delete from host").executeUpdate();

            em.persist(host);

            @SuppressWarnings({"ConstantConditions"})
            MGeneration gen = createFirstGeneration();
            et.commit();

            for (int i = 0; i < MODELLING_PERIOD; i++) {
                System.out.println(String.format("Today: %s", gen.getDate()));
                et.begin();
                gen = new MGeneration(gen).play();
                et.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (et != null) {
                et.rollback();
            }
        } finally {
            em.close();
        }
    }

    private MGeneration createFirstGeneration() {
        em.createNativeQuery("delete from friend").executeUpdate();

        return new MGeneration(this, EPOCH_START, createFirstPopulation());
    }

    private Set<MUser> createFirstPopulation() {
        final Set<MUser> res = new HashSet<MUser>();

        final int starSize = (int) Math.round(STAR_USERS * USERS);
        final int coreSize = (int) Math.round(CORE_USERS * USERS);

        while (res.size() <= starSize) {
            res.add(new MUser(this, MUser.Type.star, EPOCH_START));
        }
        while (res.size() <= starSize + coreSize) {
            res.add(new MUser(this, MUser.Type.core, EPOCH_START));
        }

        createFriends(new ArrayList<MUser>(res), FRIEND_DEGREE, EPOCH_START);

        addUsersIfRequired(EPOCH_START, res);

        return res;
    }

    private void createFriends(List<MUser> users, double targetAvg, Date date) {
        int tryCount = 0;
        double currentAvg = computeFriendDegree();
        boolean created;
        while (currentAvg < targetAvg && tryCount++ < 50) {
            created = createFriendshipRelation(users, date);
            if (created) {
                currentAvg = computeFriendDegree();
            }
        }
//        System.out.println(String.format("Actual average friend degree: %.2f", currentAvg));
    }

    private double computeFriendDegree() {
        final Number res = (Number) em.createNativeQuery("select avg(f_count) from (select count(id) f_count from friend group by user_id) tmp1").getSingleResult();
        return res != null ? res.doubleValue() : 0;
    }

    private boolean createFriendshipRelation(List<MUser> users, Date date) {
        final MUser first = users.get(rnd.nextInt(users.size()));
        final MUser second = users.get(rnd.nextInt(users.size()));
        if (first != second
                && first.canHaveFriends()
                && second.canHaveFriends()
                && !first.getFriends().contains(second)
                && first.isActive(date)
                & second.isActive(date)
                ) {
            first.becomeFriendWith(second);
            em.persist(new Friend(first.getUserId(), EPOCH_START, Action.OperationType.add, second.getUserId()));
            em.persist(new Friend(second.getUserId(), EPOCH_START, Action.OperationType.add, first.getUserId()));
            return true;
        }
        return false;
    }

    public void addUsersIfRequired(Date now, Set<MUser> users) {
        final List<MUser> userList = new ArrayList<MUser>(users);

        int added = 0;
        while (users.size() < USERS) {
            final MUser newcomer = new MUser(this, MUser.Type.newcomer, now);
            users.add(newcomer);
            added++;

            if (newcomer.canHaveFriends()) {
                final MUser inviter = userList.get(rnd.nextInt(userList.size()));
                newcomer.becomeFriendWith(inviter);

                em.persist(new Friend(newcomer.getUserId(), EPOCH_START, Action.OperationType.add, inviter.getUserId()));
                em.persist(new Friend(inviter.getUserId(), EPOCH_START, Action.OperationType.add, newcomer.getUserId()));
            }
        }
        if (added > 0) {
            System.out.println(String.format("Today (%s) added %d newcomes", now, added));
        }

        createFriends(new ArrayList<MUser>(users), FRIEND_DEGREE, now);
    }

    public Url visit(Date now, MUser user, Url url) {
        em.persist(new Browsing(user.getUserId(), now, Action.OperationType.add, url, null));
        return url;
    }

    public Url visitNewPage(Date now, MUser user) {
        final Url url = new Url(host, "/url/" + urlNumber++);
        em.persist(url);
        em.persist(new Browsing(user.getUserId(), now, Action.OperationType.add, url, null));
        return url;
    }

    public void recommend(Date now, MUser user, Url url) {
        em.persist(new Rate(user.getUserId(), now, Action.OperationType.add, url, null));
    }

    public boolean wasRecommendedBefore(String userId, long urlId, Date now) {
        final Object result = em
                .createNativeQuery("select exists(select * from rate where user_id = :USER_ID and url_id = :URL_ID and date < :DATE)")
                .setParameter("USER_ID", userId)
                .setParameter("URL_ID", urlId)
                .setParameter("DATE", now)
                .getSingleResult();

        return ((Number) result).intValue() > 0;
    }

    public String getNextUserId() {
        return Toolkit.getUserHash(userGuid++);
    }

    public int getNewcomerLifeLength() {
        return Math.max(1, (int) Math.abs(NEWCOMER_LIFE_DEVIATION * rnd().nextGaussian()));
    }

    public int guessNewPagesPerDay(MUser.Type type) {
        if (type == MUser.Type.star) {
            return Math.max(0, (int) Math.floor(STAR_NEW_PAGES_PER_DAY_BASE + Math.abs(STAR_NEW_PAGES_PER_DAY_DEVIATION * rnd().nextGaussian())));
        } else if (type == MUser.Type.core) {
            return Math.max(0, (int) Math.floor(CORE_NEW_PAGES_PER_DAY_BASE + Math.abs(CORE_NEW_PAGES_PER_DAY_DEVIATION * rnd().nextGaussian())));
        } else {
            return Math.max(0, (int) Math.floor(NEWCOMER_NEW_PAGES_PER_DAY_BASE + Math.abs(NEWCOMER_NEW_PAGES_PER_DAY_DEVIATION * rnd().nextGaussian())));
        }
    }

    public int guessRecomendationSize(MUser.Type type, int vSize) {
        final int preResult;
        if (type == MUser.Type.star) {
            preResult = (int) Math.floor(vSize * STAR_RECOMMEND + STAR_RECOMMEND_DEVIATION * rnd().nextGaussian());
        } else if (type == MUser.Type.core) {
            preResult = (int) Math.floor(vSize * CORE_RECOMMEND + CORE_RECOMMEND_DEVIATION * rnd().nextGaussian());
        } else {
            preResult = (int) Math.floor(vSize * NEWCOMER_RECOMMEND + NEWCOMER_RECOMMEND_DEVIATION * rnd().nextGaussian());
        }
        return Math.max(0, Math.min(vSize, preResult));
    }

    public int guessFollowFriendRecomendation(MUser.Type type, int rSize) {
        final int preResult;
        if (type == MUser.Type.star) {
            preResult = (int) Math.floor(rSize * STAR_FOLLOW_RECOMENDATION + STAR_FOLLOW_RECOMENDATION_DEVIATION  * rnd().nextGaussian());
        } else if (type == MUser.Type.core) {
            preResult = (int) Math.floor(rSize * CORE_FOLLOW_RECOMENDATION + CORE_FOLLOW_RECOMENDATION_DEVIATION  * rnd().nextGaussian());
        } else {
            preResult = (int) Math.floor(rSize * NEWCOMER_FOLLOW_RECOMENDATION + NEWCOMER_FOLLOW_RECOMENDATION_DEVIATION  * rnd().nextGaussian());
        }
        return Math.max(0, Math.min(rSize, preResult));
    }

    public int guessFollowPublicRecomendation(int rSize) {
        return Math.max(
                0,
                Math.min(
                        rSize,
                        (int) Math.round(FOLLOW_PUBLIC_RECOMENDATION + FOLLOW_PUBLIC_RECOMENDATION_DEVIATION * rnd().nextGaussian())
                )
        );
    }

    public Random rnd() {
        return rnd;
    }

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();
        new Generator().game();
        System.out.println("Simulation time: " + ((double) System.currentTimeMillis() - startTime) / (60 * 1000) + " minutes");

//        final Generator gen = new Generator();
//        final Map<Integer, Integer> v = new HashMap<Integer, Integer>();
//        for (int i = 0; i < 1000; i++) {
//            int t = gen.guessFollowFriendRecomendation(MUser.Type.core, 20);
//            if (v.containsKey(t)) {
//                v.put(t, v.get(t) + 1);
//            } else {
//                v.put(t, 1);
//            }
//        }
//        System.out.println(v);
    }
}
