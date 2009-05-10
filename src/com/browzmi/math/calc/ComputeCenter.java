package com.browzmi.math.calc;

import com.browzmi.math.Configuration;
import com.browzmi.math.entity.equation.WVDay;
import com.browzmi.math.entity.equation.WVHostDay;
import com.browzmi.math.entity.equation.WVHostHour;
import com.browzmi.math.entity.equation.Relation;
import com.browzmi.math.sql.SQL;
import com.browzmi.math.calc.algorithm.TCGraphBase;
import com.browzmi.math.calc.algorithm.TightlyCoupledGraph2;
import com.browzmi.math.calc.algorithm.UID;
import com.browzmi.math.calc.algorithm.TightlyCoupledGraph;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.Collections;

import gnu.trove.TLongHashSet;
import gnu.trove.THashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 25.12.2007
 * Time: 0:08:21
 */
public final class ComputeCenter {
    public static final String RELATION_TABLE = "w_v_day";
    public static final Class RELATION_CLASS = WVDay.class;
    public static final String OPERATION_TABLE = "v_day";
    public static final int MIN_VALUE = 2;
    public static final double K = 0.8;

    public static void main(String[] args) {
        final EntityManager em = Configuration.getInstance().em();
        try {
            final long start = System.currentTimeMillis();
            //noinspection unchecked
            final List<Timestamp> steps = em.createNativeQuery("select distinct step from " + OPERATION_TABLE).getResultList();
            final Query urlQuery = em.createNativeQuery(SQL.SELECT_URLS_BY_CENTER.replace(":operationTable", OPERATION_TABLE));
            final Query usersQuery = em.createNativeQuery("select count(distinct user_id) from :operationTable where step = :step".replace(":operationTable", OPERATION_TABLE));

            System.out.println("Step\t#Users\t#CommunityCount\t#UserCenter\t#UrlCenter\tUsers\tUserCenter\tUrlCenter");
            for (final Timestamp step : steps) {
                final int userCount = ((Number) usersQuery.setParameter("step", step).getSingleResult()).intValue();

                //noinspection unchecked
				final List<Relation> relations = em.createNativeQuery("select * from " + RELATION_TABLE + " where step = :step and value >= :minValue", RELATION_CLASS)
						.setParameter("step", step)
						.setParameter("minValue", MIN_VALUE)
						.getResultList();

				final TCGraphBase<List<THashSet<UID>>> allGraphs = new TightlyCoupledGraph(relations);
				final TCGraphBase<THashSet<UID>> graph = new TightlyCoupledGraph2(relations);

                final List<UID> nodes = graph.getNodes();
                final List<THashSet<UID>> allCommunities = allGraphs.process();
                final THashSet<UID> community = graph.process();
                final int urlCenterThreshhold = (int) Math.floor(K * community.size());
                final List<Number> urls;

                if (urlCenterThreshhold > 0) {
                    urlQuery.setParameter("step", step);
                    urlQuery.setParameter("center", toStringSet(community));
					//urls viewed by some patr of use center
                    urlQuery.setParameter("centerSize", urlCenterThreshhold);

                    //noinspection unchecked
                   urls = urlQuery.getResultList();
                } else {
                    urls = Collections.emptyList();
                }


                System.out.println(String.format("%s\t%d\t%d\t%d\t%d\t%s\t%s\t%s", step, userCount, allCommunities.size(), community.size(), urls.size(), nodes, community, urls));
            }
            System.out.println(String.format("Cumputed in %f.02 seconds", 1.0 * (System.currentTimeMillis() - start) / 1000));
        } finally {
            em.close();
        }
    }

	private static String[] toStringSet(THashSet<UID> result) {
		String[] res = new String[result.size()];
		int i = 0;
		for (UID uid : result) {
			res[i++] = uid.toString();
		}
		return res;
	}
}
