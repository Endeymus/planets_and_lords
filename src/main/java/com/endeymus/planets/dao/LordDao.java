package com.endeymus.planets.dao;

import com.endeymus.planets.entities.Lord;

import java.util.List;

public interface LordDao {
    List<Lord> findAll();
    Lord findById(int id);
    Lord save(Lord contract);
    Lord insert(Lord contract);
    void delete(Lord contract);
    List<Lord> findLounger();
    List<Lord> findYoung();
}
