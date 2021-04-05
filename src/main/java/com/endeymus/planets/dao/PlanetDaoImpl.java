package com.endeymus.planets.dao;

import com.endeymus.planets.entities.Lord;
import com.endeymus.planets.entities.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    @Override
    public Planet save(Planet contract) {
        Map<String, Object> param = Map.of("name", contract.getName(), "id_lord", contract.getIdLord(), "id", contract.getId());
        namedParameterJdbcTemplate.update(SQL_UPDATE, param);
        return contract;
    }

    @Override
    public Planet insert(Planet planet) {
        Map<String, Object> param = Map.of("name", planet.getName());
        namedParameterJdbcTemplate.update(SQL_INSERT, param);
        return planet;
    }

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

    @Override
    public void delete(Planet contract) {
        if (contract == null)
            throw new NullPointerException("null contract in delete");

        Map<String, Object> param = Map.of("id", contract.getId());
        namedParameterJdbcTemplate.update(SQL_DELETE, param);
    }
}
