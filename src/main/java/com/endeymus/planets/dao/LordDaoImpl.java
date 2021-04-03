package com.endeymus.planets.dao;

import com.endeymus.planets.entities.Lord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component("lordDao")
@Transactional
public class LordDaoImpl implements LordDao{

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SQL_FIND_LOUNGER =
            "select s.id, s.name, s.age from lord as s " +
            "left join planet p on s.id = p.id_lord " +
            "where p.id_lord is null";

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

    @Transactional(readOnly = true)
    @Override
    public List<Lord> findAll() {
        return customSqlFind(SQL_FIND_ALL);
    }

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


    @Override
    public Lord save(Lord contract) {
        Map<String, Object> param = Map.of("name", contract.getName(), "age", contract.getAge(), "id", contract.getId());
        int i = namedParameterJdbcTemplate.update(SQL_UPDATE, param);
        System.out.println("int i = " + i);
        return contract;
    }

    @Override
    public Lord insert(Lord contract) {
        Map<String, Object> param = Map.of("name", contract.getName(), "age", contract.getAge());
        namedParameterJdbcTemplate.update(SQL_INSERT, param);
        return contract;
    }

    @Override
    public void delete(Lord contract) {
        Map<String, Object> param = Map.of("id", contract.getId());
        namedParameterJdbcTemplate.update(SQL_DELETE, param);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Lord> findLounger() {
        return customSqlFind(SQL_FIND_LOUNGER);
    }

    @Override
    public List<Lord> findYoung() {
        return customSqlFind(SQL_FIND_YOUNG);
    }

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
