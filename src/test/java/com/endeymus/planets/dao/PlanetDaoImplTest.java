package com.endeymus.planets.dao;

import com.endeymus.planets.config.AppConfig;
import com.endeymus.planets.config.TestConfig;
import com.endeymus.planets.entities.Planet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DisplayName(value = "Тест доступа к таблице Planet")
@Transactional
@ContextConfiguration(classes = {AppConfig.class,
                                TestConfig.class})
@ExtendWith(SpringExtension.class)
class PlanetDaoImplTest {

    @Autowired
    private PlanetDao planetDao;


    @Test
    @DisplayName(value = "Должен вернуть список планет")
    void findAll() {
        List<Planet> planets = planetDao.findAll();
        assertAll("planets",
                ()-> assertNotNull(planets),
                ()-> assertEquals(11, planets.size()));
    }

    @Test
    @DisplayName(value = "Должен вернуть планету с ID 3")
    void findById() {
        Planet planet = planetDao.findById(3);
        assertNotNull(planet);
        assertAll("planet",
                () -> assertEquals("Korriban", planet.getName()),
                () -> assertEquals(3, planet.getIdLord())
        );
    }

    @Test
    @DisplayName(value = "Должен сохранить в БД измененную записать")
    void save() {
        Planet planet = planetDao.findById(2);
        assertNotNull(planet);

        planet.setName("Changed Name");
        planetDao.save(planet);

        planet = planetDao.findById(2);
        Planet finalPlanet = planet;

        assertAll("finalPlanet",
                ()-> assertNotNull(finalPlanet),
                ()-> assertEquals("Changed Name", finalPlanet.getName()),
                ()-> assertEquals(2, finalPlanet.getId()),
                ()-> assertEquals(2, finalPlanet.getIdLord())
        );


    }

    @Test
    @DisplayName(value = "Должен сохранить в базу данных новую запись")
    @Order(2)
    void insert() {
        Planet planet = new Planet();
        planet.setName("New Planet");
        planetDao.insert(planet);

        List<Planet> planets = planetDao.findAll();
        planets.forEach(System.out::println);

        Planet insertedPlanet = planetDao.findById(13);
        assertAll("insertedPlanet",
                ()-> assertNotNull(insertedPlanet),
                ()-> assertEquals("New Planet", insertedPlanet.getName())
                );

    }

    @Test
    @DisplayName(value = "Должен удалить новодобавленную Планету")
    @Order(1)
    void delete() {
        Planet newPlanet = new Planet();
        newPlanet.setName("New Planet");
        planetDao.insert(newPlanet);

        newPlanet = planetDao.findByName("New Planet");

        int count = planetDao.findAll().size();
        assertNotNull(newPlanet);
        planetDao.delete(newPlanet);
        assertEquals(count - 1, planetDao.findAll().size());
    }

    @Test
    @DisplayName(value = "Должен завершиться с ошибкой [Incorrect result size: expected 1, actual 0]")
    void findNotExist() {
        Throwable throwable = assertThrows(EmptyResultDataAccessException.class, ()->{
            Planet planet = planetDao.findById(99);
        });
        assertNotNull(throwable.getMessage());
        System.out.println(throwable.getMessage());
    }
}