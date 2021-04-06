package com.endeymus.planets.dao;

import com.endeymus.planets.entities.Lord;
import com.endeymus.planets.entities.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс осуществляющий запросы к БД
 * @author Mark Shamray
 */
@Component("lordDao")
@Transactional
public class LordDaoImpl implements LordDao{

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SQL_FIND_LOUNGER =
            "select s.id, s.name, s.age from lord as s " +
            "left join planet p on s.id = p.id_lord " +
            "where p.id_lord is null";

    private static final String SQL_FIND_ALL_WITH_PLANETS = "select s.id, s.name, s.age, a.id, a.name, a.id_lord from lord as s " +
                                                "left join planet a on s.id = a.id_lord";
    private static final String SQL_FIND_ALL = "select * from lord";
    private static final String SQL_FIND_YOUNG = "select * from lord order by age limit 10";
    private static final String SQL_FIND_BY_ID = "select * from lord where id = :id";
    private static final String SQL_UPDATE = "update lord set NAME = :name, AGE = :age WHERE id = :id";
    private static final String SQL_DELETE = "delete from lord where id = :id";
    private static final String SQL_INSERT = "insert into lord(name, age) values (:name, :age)";


    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Запрос с получение списка всех повелителей
     * @return список {@link java.util.List}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Lord> findAll() {
        return customSqlFind(SQL_FIND_ALL);
    }

    /**
     * Запрос с получением списка повелителей и планет, которыми они упраывляют
     * @return список {@link java.util.List}
     */
    @Override
    public List<Lord> findAllWithPlanets() {
        return namedParameterJdbcTemplate.query(SQL_FIND_ALL_WITH_PLANETS, resultSet -> {
            Map<Integer, Lord> map = new HashMap<>();
            Lord lord;
            while (resultSet.next()) {
                int id = resultSet.getInt("s.id");
                lord = map.get(id);
                if (lord == null) {
                    lord = new Lord();
                    lord.setId(id);
                    lord.setName(resultSet.getString("s.name"));
                    lord.setAge(resultSet.getInt("s.age"));
                    map.put(id, lord);
                }
                int planetId = resultSet.getInt("a.id");
                if (planetId > 0) {
                    Planet planet = new Planet();
                    planet.setId(planetId);
                    planet.setIdLord(id);
                    planet.setName(resultSet.getString("a.name"));
                    lord.addPlanet(planet);
                }
            }
            return new ArrayList<>(map.values());
        });

    }

    /**
     * Запрос на поиск Повелителя по идентификатору
     * @param id идентификатор Повелителя в БД
     * @return Повелителя {@link com.endeymus.planets.entities.Lord}
     */
    @Transactional(readOnly = true)
    @Override
    public Lord findById(int id) {
        Map<String, Object> param = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_ID, param, ((rs, i) -> {
            Lord lord = new Lord();
            lord.setId(rs.getInt("id"));
            lord.setName(rs.getString("name"));
            lord.setAge(rs.getInt("age"));
            return lord;
        }));
    }

    /**
     * Запрос на сохранение изменений у Повелителя
     * @param contract измененный Повелитель
     * @return Повелитель {@link com.endeymus.planets.entities.Lord}
     */
    @Override
    public Lord save(Lord contract) {
        Map<String, Object> param = Map.of("name", contract.getName(), "age", contract.getAge(), "id", contract.getId());
        int i = namedParameterJdbcTemplate.update(SQL_UPDATE, param);
        System.out.println("int i = " + i);
        return contract;
    }

    /**
     * Запрос на вставку (добавления) нового Повелителя
     * @param contract новый Повелитель
     * @return Повелитель {@link com.endeymus.planets.entities.Lord}
     */
    @Override
    public Lord insert(Lord contract) {
        Map<String, Object> param = Map.of("name", contract.getName(), "age", contract.getAge());
        namedParameterJdbcTemplate.update(SQL_INSERT, param);
        return contract;
    }

    /**
     * Запрос на удаление Повелителя по идентификатору
     * @param contract Повелитель, которого следует удалить
     */
    @Override
    public void delete(Lord contract) {
        Map<String, Object> param = Map.of("id", contract.getId());
        namedParameterJdbcTemplate.update(SQL_DELETE, param);
    }

    /**
     * Запрос на поиск всех бездельников Повелителей
     * @return список {@link java.util.List}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Lord> findLounger() {
        return customSqlFind(SQL_FIND_LOUNGER);
    }

    /**
     * Запрос на поиск ТОП 10 самых молодых Повелителей
     * @return список {@link java.util.List}
     */
    @Override
    public List<Lord> findYoung() {
        return customSqlFind(SQL_FIND_YOUNG);
    }

    /**
     * Вспомогательный метод, для получения списка Повелителей, без планет, которыми они управляют
     * @param sql SQL запрос
     * @return список {@link java.util.List}
     */
    private List<Lord> customSqlFind(String sql) {
        return namedParameterJdbcTemplate.query(sql, ((resultSet, i) -> {
            Lord lord = new Lord();
            lord.setId(resultSet.getInt("id"));
            lord.setName(resultSet.getString("name"));
            lord.setAge(resultSet.getInt("age"));
            return lord;
        }));
    }
}
