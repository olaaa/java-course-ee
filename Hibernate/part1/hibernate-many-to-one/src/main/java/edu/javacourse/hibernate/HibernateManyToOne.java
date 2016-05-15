package edu.javacourse.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Простой пример работы со связанными таблицами
 *
 * @author ASaburov
 */
public class HibernateManyToOne {

    private static final Logger log = LoggerFactory.getLogger(HibernateManyToOne.class);

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

//        testLazyInit(s);
//
        log.debug("====================================================");
//
//        List<Region> regionList = s.createQuery("from Region").list();
//
//        for (Region r : regionList) {
//            log.debug("Region name: {}", r);
//            for (City c : r.getCityList()) {
//                log.debug("     City name: {}", c);
//            }
//        }
//
        Region spb = new Region("SPb-11");
        // если закомментировать s.save(spb);
        // и завершить выплнение мейн, то можно пронаблюдать работу каскад персист
        // а именно падение исключения TransientObjectException
//        s.save(spb);


        City gatchina = new City();
        gatchina.setCityName("Gatchina-1");
        gatchina.setRegion(spb);
        s.save(gatchina);
//        return;
        log.debug("====================================================");

//        сохранение несуществующего в БД региона зависит от каскада:
//если не будет указано никакого каскада,
//        то он не будет создавать регион
//        CascadeType.PERSIST — при персисте города создаст регион
//        --------------------------------------------------------------
//        CascadeType.MERGE — при мёрдже города обновит регион (если он поменялся)
//        CascadeType.REFRESH — при рефреш города обновит регион (если он поменялся) (из бд)
//        CascadeType.REMOVE каскадно выполнится и над связанной сущностью

        s.save(spb);
        // если s.save(spb); выше закомментирован (60 строчка, то здесь выполнится следующий код)
//        регион заапдейтися!
//      - insert into public.jc_city (city_name, region_id) values (?, ?)
//        - binding parameter [1] as [VARCHAR] - [Gatchina-1]
//        - binding parameter [2] as [BIGINT] - [null]
//        - ====================================================
//        - insert into public.jc_region (region_name) values (?)
//        - binding parameter [1] as [VARCHAR] - [SPb-11]
//        - update public.jc_city set city_name=?, region_id=? where city_id=?
//        - binding parameter [1] as [VARCHAR] - [Gatchina-1]
//        - binding parameter [2] as [BIGINT] - [48]s.getTransaction().commit();
//        - binding parameter [3] as [INTEGER] - [62]log.debug("Transaction committed");
//        - Transaction committed
        destroy();
    }

    private static void testLazyInit(Session s) {
        Criteria criteria = s.createCriteria(City.class);
        List<City> cityList = criteria.list(); // только айдишники регионов, сами регионы не подтянули
//select this_.city_id as city_id1_0_0_, this_.city_name as city_nam2_0_0_, this_.region_id as region_i3_0_0_ from public.jc_city this_
        for (City city : cityList) {
            log.debug("city id: {}", city.getCityId());
            log.debug("city name: {}", city.getCityName());
            log.debug("city class: {}", city.getClass().getCanonicalName());
            // выполнение запроса, подставляет айди региона city.getRegion().getRegionId()
//            select region0_.region_id as region_i1_1_0_, region0_.region_name as region_n2_1_0_ from public.jc_region region0_ where region0_.region_id=?
            // но, если нужен регион, который уже вытаскивался, запроса не будет, этот регион сохранится в кэше
            log.debug("city region id: {}", city.getRegion().getRegionId());
            log.debug("city region name: {}", city.getRegion().getRegionName());
            log.debug("city region class: {}", city.getRegion().getClass().getCanonicalName());
            log.debug("");
        }
    }

}
