package edu.javacourse.hibernate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Простой пример для конфигурации в виде XML
 *
 * @author ASaburov
 */
public class HibernateConfigXMLMappingAnnotations {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    private static final Logger log = LoggerFactory.getLogger(HibernateConfigXMLMappingAnnotations.class);

    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    private static void init() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    private static void destroy() {
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }

    public static void main(String[] args) {
        init();

        Session s = sessionFactory.getCurrentSession();
        s.beginTransaction();
// сохранить в сессию
        s.save(new Region("Am"));

//        (region_name) - название колонки,
// айдишник не знаем, поэтому не вставляем
//        [SqlStatementLogger] - insert into public.jc_region (region_name) values (?)
//        [BasicBinder] - binding parameter [1] as [VARCHAR] - [Krasnoe Selo Sat May 14 14:04:51 SAMT 2016]



//        List<Region> regionList = s.createQuery("from Region").list();
//        for (Region r : regionList) {
//            System.out.println(r);
//        }


        // сразу обращается в базу и если вбазе нет, то кидает исключение
//        final Object load = s.load(Region.class, 11L); // кинет эксепшн
//        final Object o = s.get(Region.class, 11L); // вернёт налл
//        region0_ алиас таблицы

        // AS задаёт новый заголовок у столбца результирующей таблицы, алиас
//        -select region0_.region_id as region_i1_0_, region0_.region_name as region_n2_0_ from public.jc_region region0_
//        -extracted value([region_i1_0_]:[BIGINT])-[1]
//        -extracted value([region_n2_0_]:[VARCHAR])-[Krasnoe Selo Sat May 14 14:04:51 SAMT 2016]
//

//        фильтрация
        final Criteria criteria = s.createCriteria(Region.class);
        criteria.addOrder(Order.asc("regionName"));
        log.info(gson.toJson(criteria.list()));



        s.getTransaction().commit();

        log.debug("Transaction committed");

        destroy();
    }
}
