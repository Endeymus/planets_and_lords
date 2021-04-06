package com.endeymus.planets.dao;

import com.endeymus.planets.config.AppConfig;
import com.endeymus.planets.config.TestConfig;
import com.endeymus.planets.entities.Lord;
import org.junit.jupiter.api.DisplayName;
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

@DisplayName(value = "Тест доступа к таблице Lord")
@Transactional
@ActiveProfiles("test")
@ContextConfiguration(classes = {AppConfig.class,
                                TestConfig.class})
@ExtendWith(SpringExtension.class)
class LordDaoImplTest {

    @Autowired
    private LordDao lordDao;

    @DisplayName(value = "Должен вывести списко всех повелителей")
    @Test
    void findAll() {
        List<Lord> list = lordDao.findAll();
        assertNotNull(list);
    }

    @DisplayName(value = "Должен получить повелителя под ID 1")
    @Test
    void findById() {
        Lord lord = lordDao.findById(1);
        assertAll("lord",
                ()-> assertNotNull(lord),
                ()-> assertEquals("John Mayer", lord.getName()),
                ()-> assertEquals(1, lord.getId()),
                ()-> assertEquals(345, lord.getAge())
                );
    }
    @DisplayName(value = "Должнен сохранить изменения")
    @Test
    void save() {
        Lord lord = lordDao.findById(5);
        assertNotNull(lord);

        lord.setName("Changed Name");
        lordDao.save(lord);

        lord = lordDao.findById(5);
        Lord finalLord = lord;

        assertAll("lord",
                ()-> assertNotNull(finalLord),
                ()-> assertEquals("Changed Name", finalLord.getName()),
                ()-> assertEquals(5, finalLord.getId())
                );
    }

    @DisplayName(value = "Должен удалить одного повелителя")
    @Test
    void delete() {
        int startSize = lordDao.findAll().size();
        assertEquals(11, startSize);

        Lord lord = lordDao.findById(10);
        assertNotNull(lord);
        lordDao.delete(lord);

        assertEquals(startSize - 1, lordDao.findAll().size());
    }

    @DisplayName(value = "Должен вывести список Повелителей бездельников")
    @Test
    void findLounger() {
        List<Lord> lords = lordDao.findLounger();
        assertAll("lords",
                ()->assertEquals(3, lords.size()),
                ()->assertEquals(5, lords.get(0).getId())
        );
    }

    @Test
    @DisplayName(value = "Должен сохранить в БД новую запись")
    void insert() {
        Lord lord = new Lord();
        lord.setName("New Lord");
        lord.setAge(10000);
        lordDao.insert(lord);

        Lord insertedLord = lordDao.findById(12);
        assertAll("insertedLord",
                ()-> assertNotNull(insertedLord),
                ()-> assertEquals("New Lord", insertedLord.getName()),
                ()-> assertEquals(10000, insertedLord.getAge())
                );
    }

    @Test
    @DisplayName(value = "Должен завершиться с ошибкой [Incorrect result size: expected 1, actual 0]")
    void findNotExist() {
        Throwable throwable = assertThrows(EmptyResultDataAccessException.class, ()->{
            Lord lord = lordDao.findById(9999);
        });
        assertNotNull(throwable.getMessage());
        System.out.println(throwable.getMessage());
    }
}
