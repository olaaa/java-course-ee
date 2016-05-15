package edu.javacourse.hibernate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author ASaburov
 * @author Georgy Gobozov
 */
public class HibernateCRUD {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    private static final Logger log = LoggerFactory.getLogger(HibernateCRUD.class);

    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    private static Serializable firstCityId;
    private static Serializable secondCityId;

    private static void init() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    private static void destroy() {
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }

    static Serializable id = null;

    public static void main(String[] args) {
        init();

        create();

        get();
        load();

        getVsLoad();

        update();
        delete();

        destroy();
    }

    private static void create() {
        log.info("==============CREATE=================");
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Region region = new Region("Saint-Petersburg!");
        session.save(region);
        session.getTransaction().commit();
        id = region.getRegionId();
    }

    private static void get() {
        log.info("==============GET=================");
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Region region = (Region) session.get(Region.class, id);
        log.info("region = {}", region);
        session.getTransaction().commit();
    }

    private static void load() {
        log.info("==============LOAD=================");
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Region region = (Region) session.load(Region.class, id);
        log.info("region = {}", region);
        session.getTransaction().commit();
    }

/*Based on the above explanations we have following differences between get() vs load():

get() loads the data as soon as it’s called whereas load() returns a proxy object and loads data only when it’s actually required, so load() is better because it support lazy loading.
Since load() throws exception when data is not found, we should use it only when we know data exists.
We should use get() when we want to make sure data exists in the database.*/
    private static void getVsLoad() {
        log.info("==============GET_VS_LOAD=================");
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Region region = (Region) session.get(Region.class, id);
        City city1 = new City();
        city1.setCityName("Surgut");
        city1.setRegion(region);
        firstCityId = session.save(city1); // выполнился инсерт
//        insert into public.jc_city(city_name, region_id) values( ?,?)
        region = (Region) session.load(Region.class, id); // select не выполнился
        City city2 = new City();
        city2.setCityName("Nizhnevartovsk");
        city2.setRegion(region);
        secondCityId = session.save(city2); // выполнился инсерт
//        insert into public.jc_city(city_name, region_id) values( ?,?)
        region = (Region) session.get(Region.class, id); // select не выполнился

        session.getTransaction().commit(); // отобразились изменения в базе
    }

    private static void update() {
        log.info("==============UPDATE=================");
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        // load — будет прокси-тип, get — просто City
        City city = (City) session.get(City.class, firstCityId); // здесь будет прокси-тип
        city.setCityName("nefteugansk");
        session.saveOrUpdate(city); // insert не выполнгился
        log.info("city = {}", city);

        session.getTransaction().commit();// выполнился update
//        update public.jc_city set city_name=?, region_id=? where city_id=?
//        binding parameter[ 1]as[VARCHAR] -[nefteugansk]
//        binding parameter[ 2]as[BIGINT] -[32]
//        binding parameter[ 3]as[INTEGER] -[41]
    }


    private static void delete() {
        log.info("==============DELETE=================");
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        City city = (City) session.load(City.class, id);
        session.delete(city);

        City city2 = (City) session.load(City.class, secondCityId);
        session.delete(city2);

        session.getTransaction().commit(); // выполнились оба делита
    }
}
