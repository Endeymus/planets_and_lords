package com.endeymus.planets.dao;

import com.endeymus.planets.entities.Planet;

import java.util.List;

public interface PlanetDao {
    List<Planet> findAll();
    Planet findById(int id);
    Planet findByName(String name);
    Planet save(Planet contract);
    Planet insert(Planet contract);
    void delete(Planet contract);
}
