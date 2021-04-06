package com.endeymus.planets.dao;

import com.endeymus.planets.entities.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Класс осуществляющий запросы к БД
 * @author Mark Shamray
 */
@Component(value = "planetDao")
@Transactional
public class PlanetDaoImpl implements PlanetDao{

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SQL_FIND_ALL = "select * from planet";
    private static final String SQL_FIND_BY_ID = "select * from planet where id = :id";
    private static final String SQL_UPDATE = "update planet set NAME = :name, ID_LORD = :id_lord where id = :id";
    private static final String SQL_DELETE = "delete from planet where id = :id";
    private static final String SQL_INSERT = "insert into planet(name) values (:name)";

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Запрос на поиск всех планет
     * @return список {@link java.util.List}
     */
    @Override
    public List<Planet> findAll() {
        return namedParameterJdbcTemplate.query(SQL_FIND_ALL, ((resultSet, i) ->  {
            Planet planet = new Planet();
            planet.setId(resultSet.getInt("id"));
            planet.setName(resultSet.getString("name"));
            planet.setIdLord(resultSet.getInt("id_lord"));
            return planet;
        }));
    }

    /**
     * Запрос на поиск планеты по идентификатору
     * @param id идентификатор планеты
     * @return Планета
     */
    @Override
    public Planet findById(int id) {
        Map<String, Object> param = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_ID, param, (resultSet, i) -> {
            Planet planet = new Planet();
            planet.setId(resultSet.getInt("id"));
            planet.setName(resultSet.getString("name"));
            planet.setIdLord(resultSet.getInt("id_lord"));
            return planet;
        });
    }

    /**
     * Запрос на сохранение изменений в планете
     * @param contract Планета {@link com.endeymus.planets.entities.Planet} измененная
     * @return Планета
     */
    @Override
    public Planet save(Planet contract) {
        Map<String, Object> param = Map.of("name", contract.getName(), "id_lord", contract.getIdLord(), "id", contract.getId());
        namedParameterJdbcTemplate.update(SQL_UPDATE, param);
        return contract;
    }

    /**
     * Запрос на вставку (добавление) новой планеты
     * @param planet Планета {@link com.endeymus.planets.entities.Planet} новая
     * @return Планета
     */
    @Override
    public Planet insert(Planet planet) {
        Map<String, Object> param = Map.of("name", planet.getName());
        namedParameterJdbcTemplate.update(SQL_INSERT, param);
        return planet;
    }

    /**
     * Запрос на поиск планеты по имени
     * @param name имя планеты
     * @return Планета
     */
    public Planet findByName(String name) {
        Map<String, Object> param = Map.of("name", name);
        return namedParameterJdbcTemplate.queryForObject("select * from planet where name = :name", param, ((resultSet, i) -> {
            Planet planet = new Planet();
            planet.setId(resultSet.getInt("id"));
            planet.setName(resultSet.getString("name"));
            planet.setIdLord(resultSet.getInt("id_lord"));
            return planet;
        }));
    }

    /**
     * Запрос на удаление планеты
     * @param contract Планета {@link com.endeymus.planets.entities.Planet} которую следует удалить
     */
    @Override
    public void delete(Planet contract) {
        if (contract == null)
            throw new NullPointerException("null contract in delete");

        Map<String, Object> param = Map.of("id", contract.getId());
        namedParameterJdbcTemplate.update(SQL_DELETE, param);
    }
}
