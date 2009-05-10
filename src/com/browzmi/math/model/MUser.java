package com.browzmi.math.model;

import com.browzmi.math.entity.Url;

import java.util.*;

public final class MUser {
    public static enum Type {
        star, core, newcomer
    }

    private final Type type;
    private final boolean canHaveFriends;
    private final boolean canRecommend;
    private final boolean recommendJustToTry;
    private boolean hasRecommendations;
    private final Generator gen;
    private final String userId;
    private final Date birthday;
    private final int lifeDays;
    private final Map<Date, Boolean> activeDays;

    private final Set<MUser> friends;

    public MUser(Generator gen, Type type, Date birthday) {
        this.gen = gen;
        this.type = type;
        this.canHaveFriends = type == Type.star || type == Type.core || guessCanHaveFriends();
        this.canRecommend = type == Type.star || type == Type.core || guessCanRecommend();
        this.recommendJustToTry = type == Type.newcomer && guessRecomendJustToCheck();

        this.userId = gen.getNextUserId();
        this.birthday = birthday;
        this.lifeDays = type == Type.newcomer ? gen.getNewcomerLifeLength() : -1;

        this.friends = new HashSet<MUser>();
        this.activeDays = type == Type.core ? new HashMap<Date, Boolean>() : Collections.<Date, Boolean>emptyMap();
    }

    //todo move to Generator
    private boolean guessCanHaveFriends() {
        return gen.rnd().nextDouble() < Generator.NEWCOMER_CAN_HAVE_FRIENDS;
    }

    //todo move to Generator
    private boolean guessCanRecommend() {
        if (canHaveFriends) {
            return gen.rnd().nextFloat() < Generator.NEWCOMER_WF_CAN_RECOMMEND;
        } else {
            return gen.rnd().nextFloat() < Generator.NEWCOMER_WOF_CAN_RECOMMEND;
        }
    }

    //todo move to Generator
    private boolean guessRecomendJustToCheck() {
        return gen.rnd().nextFloat() < Generator.NEWCOMER_RECOMMEND_JUST_TO_TRY;
    }

    public void becomeFriendWith(MUser user) {
        if (!canHaveFriends) throw new RuntimeException();
        friends.add(user);
        user.friends.add(this);
    }

    public String getUserId() {
        return userId;
    }

    public Set<MUser> getFriends() {
        return friends;
    }

    public Type getType() {
        return type;
    }

    public boolean isAlive(Date now) {
        return (type == Type.star) || (type == Type.core) || (birthday.getTime() + lifeDays * Generator.DAY) > now.getTime();
    }

    public boolean isActive(Date date) {
        if (type == Type.star || type == Type.newcomer) {
            return true;
        } else {
            if (!activeDays.containsKey(date)) {
                //todo extract to method and move to Generator
                activeDays.put(date, gen.rnd().nextFloat() < Generator.CORE_ACTIVE_DAY);
                /*
                final Calendar cal = new GregorianCalendar();
                cal.setTime(date);
                final int dow = cal.get(Calendar.DAY_OF_WEEK);
                if (dow == Calendar.SATURDAY || dow == Calendar.SUNDAY) {
                    activeDays.put(date, false);
                } else {
                    activeDays.put(date, gen.rnd().nextFloat() < Generator.CORE_ACTIVE_DAY);
                }
                */
            }
            return activeDays.get(date);
        }
    }

    public boolean canHaveFriends() {
        return canHaveFriends;
    }

    public void play(MGeneration day) {
        if (isActive(day.getDate())) {
            final Set<Url> visited = new HashSet<Url>();

            visitRecommendations(visited, day);

            visit(visited, day);

            if (canRecommend) {
                recommend(visited, day);
            }
        }
    }

    private void recommend(Set<Url> viewed, MGeneration day) {
        if (type == Type.newcomer && recommendJustToTry && hasRecommendations) return;

        final HashMap<Url, Integer> rec = new HashMap<Url, Integer>();
        final List<Url> urls = new ArrayList<Url>(viewed);
        final int vSize = viewed.size();
        final int target = gen.guessRecomendationSize(type, vSize);
        final int maxTry = (int) (1.2 * target);
        int tryCount = 0;
        while (rec.size() < target && tryCount++ < maxTry) {
            final Url url = urls.get(gen.rnd().nextInt(vSize));
            if (rec.containsKey(url)) {
                if (rec.get(url) < 3) {
                    day.recommend(this, url);
                    rec.put(url, rec.get(url) + 1);
                } else {
                    System.out.println("Try to recommend more, then 3 times the same url");
                }
            } else if (!day.wasRecommendedBefore(userId, url)) {
                day.recommend(this, url);
                rec.put(url, 1);
            }
        }
        hasRecommendations = rec.size() > 0;
    }

    private void visit(Set<Url> visited, MGeneration day) {
        final int viewSize = gen.guessNewPagesPerDay(type);
        for (int i = 0; i < viewSize; i++) {
            visited.add(day.visitNewPage(this));
        }
    }

    private void visitRecommendations(Set<Url> visited, MGeneration day) {
        visitFriendRecommendations(visited, day);
        visitPublicRecommendations(visited, day);
    }

    private void visitFriendRecommendations(Set<Url> visited, MGeneration day) {
        final MGeneration.DayRecommendations rec = day.getFriendRecommendations(this);
        final int target = gen.guessFollowFriendRecomendation(type, rec.urls.size());
        final int recListSize = rec.recommendations.size();
        int reallyVisited = 0;
        while (reallyVisited < target) {
            final Url url = rec.recommendations.get(gen.rnd().nextInt(recListSize));
            if (!visited.contains(url)) {
                day.visit(this, url);
                visited.add(url);
                reallyVisited++;
            }
        }
    }

    private void visitPublicRecommendations(Set<Url> visited, MGeneration day) {
        final MGeneration.DayRecommendations rec = day.getPublicRecommendations();
        final int target = gen.guessFollowPublicRecomendation(rec.urls.size());
        final int recListSize = rec.recommendations.size();
        int reallyVisited = 0;
        while (reallyVisited < target) {
            final Url url = rec.recommendations.get(gen.rnd().nextInt(recListSize));
            if (!visited.contains(url)) {
                day.visit(this, url);
                visited.add(url);
                reallyVisited++;
            }
        }
    }

    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && userId.equals(((MUser) o).userId);
    }

    public int hashCode() {
        return userId.hashCode();
    }
}
