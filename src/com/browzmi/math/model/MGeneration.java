package com.browzmi.math.model;

import com.browzmi.math.entity.Url;
import com.browzmi.util.Pair;

import java.util.*;

public final class MGeneration {
    private final Generator gr;
    private final MGeneration past;
    private final Date now;
    private final Set<MUser> users;
    private final DayRecommendations pastRecommendations;
    private final Map<MUser, List<Url>> actions = new HashMap<MUser, List<Url>>();

    public MGeneration(Generator gr, Date startDate, Set<MUser> firstUsers) {
        this.gr = gr;
        this.now = startDate;
        this.users = firstUsers;
        this.past = null;
        this.pastRecommendations = new DayRecommendations(Collections.<Url>emptySet(), Collections.<Url>emptyList());
    }

    public MGeneration(MGeneration past) {
        this.gr = past.gr;
        this.now = new Date(past.now.getTime() + Generator.DAY);
        this.users = filterAlive(past.users, now);
        this.past = past;
        this.pastRecommendations = extractAllRecomendations(past.actions);
    }

    private static Set<MUser> filterAlive(Set<MUser> users, Date now) {
        final Set<MUser> res = new HashSet<MUser>(users);
        for (final MUser user : users) {
            if (!user.isAlive(now)) {
                for (final MUser friend : user.getFriends()) {
                    friend.getFriends().remove(user);
                }
                res.remove(user);
            }
        }
        return res;
    }

    private DayRecommendations extractAllRecomendations(Map<MUser, List<Url>> actions) {
        final Set<Url> set = new HashSet<Url>();
        final List<Url> list = new ArrayList<Url>();
        for (final List<Url> recommendations : actions.values()) {
            set.addAll(recommendations);
            list.addAll(recommendations);
        }
        return new DayRecommendations(set, list);
    }

    private void fillActions() {
        for (final MUser user : users) {
            actions.put(user, new ArrayList<Url>());
        }
    }

    public MGeneration play() {
        gr.addUsersIfRequired(now, users);
        fillActions();

        for (final MUser user : users) {
            user.play(this);
        }

        return this;
    }

    public Date getDate() {
        return now;
    }

    public DayRecommendations getFriendRecommendations(MUser forUser) {
        final Set<Url> set = new HashSet<Url>();
        final List<Url> list = new ArrayList<Url>();
        if (past != null) {
            for (final MUser friend : forUser.getFriends()) {
                final List<Url> pastActions = this.past.actions.get(friend);
                if (pastActions != null) {
                    set.addAll(pastActions);
                    list.addAll(pastActions);
                }
            }
        }
        return new DayRecommendations(set, list);
    }

    public DayRecommendations getPublicRecommendations() {
        return pastRecommendations;
    }

    public final void recommend(MUser user, Url url) {
        actions.get(user).add(url);
        
        gr.recommend(new Date(now.getTime() + 30000), user, url);
    }

    public final Url visit(MUser user, Url url) {
        return gr.visit(new Date(now.getTime() + 10000), user, url);
    }

    public final Url visitNewPage(MUser user) {
        return gr.visitNewPage(new Date(now.getTime() + 20000), user);
    }

    public final boolean wasRecommendedBefore(String userId, Url url) {
        return gr.wasRecommendedBefore(userId, url.getId(), now);
    }

    public final static class DayRecommendations {
        public final Set<Url> urls;
        public final List<Url> recommendations;

        public DayRecommendations(Set<Url> urls, List<Url> recommendations) {
            this.urls = urls;
            this.recommendations = recommendations;
        }
    }
}
