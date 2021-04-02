package com.endeymus.planets.dao;

import com.endeymus.planets.config.AppConfig;
import com.endeymus.planets.entities.Lord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DisplayName(value = "Тест доступа к таблице Lord")
@Transactional
class LordDaoImplTest {

    private static ApplicationContext ctx;
    private static LordDao lordDao;

    @BeforeAll
    static void setUp() {
        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        lordDao = ctx.getBean("lordDao", LordDao.class);
    }

    @Test
    void findAll() {
        List<Lord> list = lordDao.findAll();
        list.forEach(System.out::println);
    }

    @Test
    void findById() {
        Lord lord = lordDao.findById(1);
        System.out.println(lord);
    }

    @Test
    void save() {

    }

    @Test
    void delete() {
    }

    @Test
    void findLounger() {
        List<Lord> list = lordDao.findLounger();
        list.forEach(System.out::println);
    }
}