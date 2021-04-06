package com.endeymus.planets.controllers;

import com.endeymus.planets.dao.PlanetDao;
import com.endeymus.planets.entities.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/planets")
public class PlanetController {

    private PlanetDao planetDao;

    @Autowired
    public void setPlanetDao(PlanetDao planetDao) {
        this.planetDao = planetDao;
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("alert") String alert,
            Model model) {
        model.addAttribute("planet", new Planet());
        model.addAttribute("alert", alert);
        return "planets/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("planet") Planet planet, BindingResult result,
                      Model model, RedirectAttributes attributes) {
        model.addAttribute("planet", planet);
        planetDao.insert(planet);
        if (!result.hasErrors())
            attributes.addFlashAttribute("alert", "success");
        else
            attributes.addFlashAttribute("alert", "failed");
        return "redirect:add";
    }

    @GetMapping("/delete")
    public String delete(@ModelAttribute("alert") String alert, Model model) {
        model.addAttribute("planets", planetDao.findAll());
        model.addAttribute("alert", alert);
        return "planets/delete";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("id") Integer id,
                         RedirectAttributes attributes ) {
        Planet planet = planetDao.findById(id);
        planetDao.delete(planet);
        attributes.addFlashAttribute("alert", "success");
        return "redirect:delete";
    }

}
