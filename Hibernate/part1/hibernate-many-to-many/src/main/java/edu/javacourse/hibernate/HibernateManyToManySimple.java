package edu.javacourse.hibernate;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Простой пример для демонстрации иерархии
 *
 * @author ASaburov
 */
public class HibernateManyToManySimple {

    private static final Logger log = LoggerFactory.getLogger(HibernateManyToMany.class);

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

// очис
//        delete from public.jc_book_author where (book_id) in(select book_id from public.jc_book)
//        delete from public.jc_book
        String hql = "delete from Book ";
        Query query = s.createQuery(hql);
        query.executeUpdate();

//        delete from public.jc_book_author where (author_id) in(select author_id from public.jc_author)
//        delete from public.jc_author
        String hqll = "delete from Author ";
        Query deleteAuthor = s.createQuery(hqll);
        deleteAuthor.executeUpdate();

        Author bloch = new Author();
        bloch.setAuthorName("Bloch");
        s.save(bloch);

        Book java = new Book();
        java.setBookName("Java");
        java.addAuthor(bloch);
        s.save(java);
        // Вариант добавления новой книги и существуюего автора
//        java.addAuthor(bloch);

        List<Book> bookList = s.createCriteria(Book.class).list();
        for (Book book : bookList) {
            log.debug("");
            log.debug("Book: {}", book);
            for (Author author : book.getAuthorList()) {
                log.debug("Author: {}", author);
            }
        }


        s.getTransaction().commit();

        log.debug("Transaction committed");

        destroy();
    }
}
