package com.browzmi.math.calc;

import com.browzmi.math.Configuration;
import com.browzmi.math.sql.SQL;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityTransaction;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 13.12.2007
 * Time: 1:11:31
 */
public final class ComputeRelation {
	private static final String COMPUTE_SCRIPT = SQL.COMPUTE_RELATION_BY_UNIQUE_URLS;

    public static void main(String[] args) {
        final EntityManager em = Configuration.getInstance().em();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();

            et.begin();
            em.createNativeQuery(SQL.DELETE_ALL.replace(":table", "w_v_hour")).executeUpdate();
            em.createNativeQuery(
                    COMPUTE_SCRIPT
                    .replace(":relationTable", "w_v_hour")
                    .replace(":sourceTable", "v_hour")
            ).executeUpdate();
            et.commit();

            et.begin();
            em.createNativeQuery(SQL.DELETE_ALL.replace(":table", "w_v_host_hour")).executeUpdate();
            em.createNativeQuery(
                    COMPUTE_SCRIPT
                    .replace(":relationTable", "w_v_host_hour")
                    .replace(":sourceTable", "v_host_hour")
            ).executeUpdate();
            et.commit();

//            em.createNativeQuery(
//                    SQL.COMPUTE_RELATION
//                    .replace(":relationTable", "w_a_hour")
//                    .replace(":sourceTable", "a_hour")
//            ).executeUpdate();

            et.begin();
            em.createNativeQuery(SQL.DELETE_ALL.replace(":table", "w_v_day")).executeUpdate();
            em.createNativeQuery(
                    COMPUTE_SCRIPT
                    .replace(":relationTable", "w_v_day")
                    .replace(":sourceTable", "v_day")
            ).executeUpdate();
            et.commit();

//            em.createNativeQuery(
//                    SQL.COMPUTE_RELATION
//                    .replace(":relationTable", "w_a_day")
//                    .replace(":sourceTable", "a_day")
//            ).executeUpdate();

            et.begin();
            em.createNativeQuery(SQL.DELETE_ALL.replace(":table", "w_v_host_day")).executeUpdate();
            em.createNativeQuery("delete from v_host_day where url_id = 1").executeUpdate();
            em.createNativeQuery(
                    COMPUTE_SCRIPT
                    .replace(":relationTable", "w_v_host_day")
                    .replace(":sourceTable", "v_host_day")
            ).executeUpdate();
            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (et != null) {
                et.rollback();
            }
        } finally {
            em.close();
        }
    }
}
