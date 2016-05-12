package edu.javacourse.spring.test;

import edu.javacourse.spring.bean.RegionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

/**
 * RunWith аннот junit. Вставляется класс, который junit будет использовать
 * У спринга есть спец класс, зависимость spring-test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springExample.xml"})
public class SimpleTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    RegionManager manager;


    @Test
    public void testRegionManager() throws Exception {
        assertNotNull(manager);
        manager.createRegion("SPB");
    }


}
