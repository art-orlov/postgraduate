package com.browzmi.math;

import com.browzmi.math.entity.*;
import com.browzmi.math.entity.equation.*;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernatePersistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.Properties;

/*
* Project: Amigo4 (beta)
* Author: ArtemOrlov
* Created: 09.12.2007 17:23:39
* 
* Copyright (c) 1999-2007 Magenta Corporation Ltd. All Rights Reserved.
* Magenta Technology proprietary and confidential.
* Use is subject to license terms.
*/
public final class Configuration {
    private Properties settings;
    private EntityManagerFactory emf;

    private Configuration() {
        try {
            this.settings = new Properties();
            settings.load(getClass().getResourceAsStream("config.properties"));

            this.emf = createFactory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties getHibernateProperties() {
        final Properties props = new Properties();

        props.setProperty(Environment.DIALECT, MySQL5InnoDBDialect.class.getName());
        props.setProperty(Environment.SHOW_SQL, "false");
        props.setProperty(Environment.HBM2DDL_AUTO, "update");

        props.setProperty(Environment.USE_STREAMS_FOR_BINARY, "true");
        props.setProperty(Environment.USE_GET_GENERATED_KEYS, "true");
        props.setProperty(Environment.USE_REFLECTION_OPTIMIZER, "true");
        props.setProperty(Environment.AUTOCOMMIT, "false");

        props.setProperty(HibernatePersistence.TRANSACTION_TYPE, "RESOURCE_LOCAL");
        
        return props;
    }

    private MysqlDataSource createDataSource() {
        try {
            Class.forName(settings.getProperty("db.driver")); //load driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        final MysqlDataSource ds = new MysqlDataSource();
        ds.setURL(settings.getProperty("db.server") + "/" + settings.getProperty("db.database"));
        ds.setUser(settings.getProperty("db.user"));
        ds.setPassword(settings.getProperty("db.password"));
        return ds;
    }

    private EntityManagerFactory createFactory() {
        final Ejb3Configuration cfg = new Ejb3Configuration();

        cfg.addProperties(getHibernateProperties());

        cfg.addAnnotatedClass(Action.class);
        cfg.addAnnotatedClass(Browsing.class);
        cfg.addAnnotatedClass(Clip.class);
        cfg.addAnnotatedClass(Comment.class);
        cfg.addAnnotatedClass(Favorite.class);
        cfg.addAnnotatedClass(FavoriteTag.class);
        cfg.addAnnotatedClass(Friend.class);
        cfg.addAnnotatedClass(Host.class);
        cfg.addAnnotatedClass(Message.class);
        cfg.addAnnotatedClass(Rate.class);
        cfg.addAnnotatedClass(Tag.class);
        cfg.addAnnotatedClass(Url.class);

		addTCGClasses(cfg);

        cfg.setDataSource(createDataSource());

        return cfg.buildEntityManagerFactory();
    }

	private static void addTCGClasses(Ejb3Configuration cfg) {
		cfg.addAnnotatedClass(OKey.class);
		cfg.addAnnotatedClass(Operation.class);

		cfg.addAnnotatedClass(View.class);
		cfg.addAnnotatedClass(VHour.class);
		cfg.addAnnotatedClass(VDay.class);
		cfg.addAnnotatedClass(VHostHour.class);
		cfg.addAnnotatedClass(VHostDay.class);

		cfg.addAnnotatedClass(WVHour.class);
		cfg.addAnnotatedClass(WVDay.class);
		cfg.addAnnotatedClass(WVHostHour.class);
		cfg.addAnnotatedClass(WVHostDay.class);
	}

	public EntityManager em() {
        return emf.createEntityManager();
    }

    private static class LazyHolder {
        static final Configuration instance = new Configuration();
    }

    public static Configuration getInstance() {
        return LazyHolder.instance;
    }
}
