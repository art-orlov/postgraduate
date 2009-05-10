package com.browzmi.math.calc;

import com.browzmi.math.Configuration;
import com.browzmi.math.sql.SQL;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.GregorianCalendar;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 17.12.2007
 * Time: 0:51:44
 */
public final class ComputeOperations {
    public static void main(String[] args) {
        final EntityManager em = Configuration.getInstance().em();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();

            et.begin();
            em.createNativeQuery(SQL.DROP_FN).executeUpdate();
            em.createNativeQuery(SQL.CREATE_FN).executeUpdate();
            et.commit();

            et.begin();
			em.createNativeQuery(SQL.DELETE_ALL.replace(":table", "view")).executeUpdate();
            em.createNativeQuery(SQL.CREATE_VIEW)
					.setParameter("FROM_DATE", new GregorianCalendar(2008, 3, 1))
					.setParameter("TO_DATE", new GregorianCalendar(2008, 4, 16))
					.executeUpdate();
            et.commit();

            et.begin();
            em.createNativeQuery(SQL.DELETE_ALL.replace(":table", "v_hour")).executeUpdate();
            em.createNativeQuery(
                    SQL.COMPUTE_OPERATION
                    .replace(":step", "DATE_SUB(date, INTERVAL (60 * EXTRACT(MINUTE FROM date) + EXTRACT(SECOND FROM date)) SECOND)")
                    .replace(":operationTable", "v_hour")
                    .replace(":sourceTable", "view")
            ).executeUpdate();
            et.commit();

            et.begin();
            em.createNativeQuery(SQL.DELETE_ALL.replace(":table", "v_host_hour")).executeUpdate();
            em.createNativeQuery(
                    SQL.COMPUTE_OPERATION_BY_HOST
                    .replace(":step", "DATE_SUB(date, INTERVAL (60 * EXTRACT(MINUTE FROM date) + EXTRACT(SECOND FROM date)) SECOND)")
                    .replace(":operationTable", "v_host_hour")
                    .replace(":sourceTable", "view")
            ).executeUpdate();
            et.commit();

//            et.begin();
//            em.createNativeQuery(
//                    SQL.COMPUTE_OPERATION
//                    .replace(":step", "DATE_SUB(date, INTERVAL (60 * EXTRACT(MINUTE FROM date) + EXTRACT(SECOND FROM date)) SECOND)")
//                    .replace(":operationTable", "a_hour")
//                    .replace(":sourceTable", "action")
//            ).executeUpdate();
//            et.commit();

            et.begin();
            em.createNativeQuery(SQL.DELETE_ALL.replace(":table", "v_day")).executeUpdate();
            em.createNativeQuery(
                    SQL.COMPUTE_OPERATION
                    .replace(":step", "fnComputeDay(date)")
                    .replace(":operationTable", "v_day")
                    .replace(":sourceTable", "view")
            ).executeUpdate();
            et.commit();

//            et.begin();
//            em.createNativeQuery(
//                    SQL.COMPUTE_OPERATION
//                    .replace(":step", "fnComputeDay(date)")
//                    .replace(":operationTable", "a_day")
//                    .replace(":sourceTable", "action")
//            ).executeUpdate();
//            et.commit();

            et.begin();
            em.createNativeQuery(SQL.DELETE_ALL.replace(":table", "v_host_day")).executeUpdate();
            em.createNativeQuery(
                    SQL.COMPUTE_OPERATION_BY_HOST
                    .replace(":step", "fnComputeDay(date)")
                    .replace(":operationTable", "v_host_day")
                    .replace(":sourceTable", "view")
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
