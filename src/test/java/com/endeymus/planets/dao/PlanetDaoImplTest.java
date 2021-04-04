package com.endeymus.planets.dao;

import com.endeymus.planets.config.AppConfig;
import com.endeymus.planets.entities.Planet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "Тест доступа к таблице Planet")
@Transactional
@Rollback
class PlanetDaoImplTest {

    private static ApplicationContext ctx;
    private static PlanetDao planetDao;

    @BeforeAll
    static void setUp() {
        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        planetDao = ctx.getBean("planetDao", PlanetDao.class);
    }

    @Test
    @DisplayName(value = "Должен вернуть список планет")
    void findAll() {
        List<Planet> planets = planetDao.findAll();
        assertNotNull(planets);
        assertEquals(5, planets.size());
    }

    @Test
    @DisplayName(value = "Должен вернуть Korriban и 3")
    void findById() {
        Planet planet = planetDao.findById(3);
        assertNotNull(planet);
        assertAll("planet",
                () -> assertEquals("Korriban", planet.getName()),
                () -> assertEquals(3, planet.getIdLord())
        );
        System.out.println(planet.getName() + " " + planet.getIdLord());
    }

    @Test
    @DisplayName(value = "Должен ")
    void save() {
    }

    @Test
    @DisplayName(value = "Должен сохранить в базу данных новую запись")
    @Rollback
    void insert() {
        List<Planet> planets = planetDao.findAll();
        assertNotNull(planets);
        assertEquals(5, planets.size());
        Planet planet = new Planet();
        planet.setName("Dathomir");
        planetDao.insert(planet);
        planets = planetDao.findAll();
        assertEquals(6, planets.size());
        planets.forEach(System.out::println);
    }

    @Test
    void delete() {

    }
}