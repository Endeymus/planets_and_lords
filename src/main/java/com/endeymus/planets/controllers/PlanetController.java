package com.endeymus.planets.controllers;

import com.endeymus.planets.dao.PlanetDao;
import com.endeymus.planets.entities.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/planets")
public class PlanetController {

    private PlanetDao planetDao;

    @Autowired
    public void setPlanetDao(PlanetDao planetDao) {
        this.planetDao = planetDao;
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("planet", new Planet());
        return "planets/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("planet") Planet planet, Model model) {
        model.addAttribute("planet", planet);
        planetDao.insert(planet);
        return "redirect:/planets/add";
    }

    @GetMapping("/delete")
    public String delete(Model model) {
        model.addAttribute("planets", planetDao.findAll());
        return "planets/delete";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("id") Integer id) {
        Planet planet = planetDao.findById(id);
        planetDao.delete(planet);
        return "redirect:/planets/delete";
    }

}
