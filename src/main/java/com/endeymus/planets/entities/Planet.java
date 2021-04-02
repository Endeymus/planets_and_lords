package com.endeymus.planets.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Planet implements Serializable {
    private int id;
    private String name;
    private int idLord;

    @Override
    public String toString() {
        return "id: " + id + ", name: " + name;
    }
}
