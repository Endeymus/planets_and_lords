package com.endeymus.planets.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Lord implements Serializable {
    private int id;
    private String name;
    private int age;

    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + ", age: " + age;
    }
}
