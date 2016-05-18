package edu.javacourse.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author ASaburov
 */
public class HibernateSecondLevelCache {

    private static final Logger log = LoggerFactory.getLogger(HibernateSecondLevelCache.class);

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

        // Делаем дважды для демонстрации - SELECT вызывается один раз или два
        // в зависимости от настроек кэша
        // ДЛЯ ТЕСТИРОВАНИЯ   QUERY CASH ОТКЛЮЧИТЬ SLC
//        <property name = "cache.use_second_level_cache" > false </property >
        checkQuery(sessionFactory);
        log.info("=============================");
        // в query cache сохранились айдишники регионов, кот мы доставали
        checkQuery(sessionFactory); // здесь сделает селектов по каждому айдишников (чтобы достать остальные поля)
        // ЕСЛИ ВКЛЮЧИТЬ SLC:
        // айдишники сохранятся в кэше запросов, а сами объекты - в SLC
        // то есть выполнится только один селекст, при первом вызове метода

        // Делаем дважды для демонстрации - SELECT вызывается один раз или два
        // в зависимости от настроек кэша
//        checkOne(sessionFactory);
//        checkOne(sessionFactory); // http://stackoverflow.com/questions/4040761/control-the-hibernate-sessionwhen-to-close-it-manually
        // селект произойдёт только один, так как отработает кэш второго уровня, так как фэктори одна и та же

        // Обращение к статистике только при включенном кэше
        showStatistics(sessionFactory);

        destroy();
    }

    private static void checkQuery(SessionFactory sessionFactory) throws HibernateException {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query createQuery = session.createQuery("from Region");
        // Если не ставить кэширование, то запрос будет дважды
        // И обязательно в hibernate.cfg.xml дожно быть установлено 
        // <property name="cache.use_query_cache">true</property>
        // Если включить без кэша - то интересный эффект
        createQuery.setCacheable(true);
        List<Region> regionList1 = createQuery.list();
//        в кэше запросов сохранится только айдишники регионов
        for (Region r : regionList1) {
            log.info("Region: {}", r);
        }
        session.getTransaction().commit();
    }

    private static void checkOne(SessionFactory sessionFactory) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Region r = (Region) session.get(Region.class, 1L); // помещается в кэш-первого уровня

        log.info("session hash {}", session.hashCode());
        log.info("Region: {}", r);
        session.getTransaction().commit();
    }

    private static void showStatistics(SessionFactory sessionFactory) {
        if (sessionFactory.getStatistics().getSecondLevelCacheStatistics("CacheForRegion") != null) {
            Map ce = sessionFactory.getStatistics().getSecondLevelCacheStatistics("CacheForRegion").getEntries();

            for (Iterator en = ce.keySet().iterator(); en.hasNext(); ) {
                Object key = en.next();
                Object value = ce.get(key);
                log.info("Key: {}", key);
                log.info("Value: {}", value);
            }
        } else {
            log.info("No statistics for second level");
        }
    }

}
