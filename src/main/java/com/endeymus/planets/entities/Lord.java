package com.endeymus.planets.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Lord implements Serializable {
    private int id;
    private String name;
    private int age;
    private List<Planet> list;

    public Lord() {
        list = new ArrayList<>();
    }

    public Lord(String name, int age) {
        this.name = name;
        this.age = age;
        list = new ArrayList<>();
    }

    public void addPlanet(Planet planet) {
        list.add(planet);
    }

    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + ", age: " + age;
    }
}
