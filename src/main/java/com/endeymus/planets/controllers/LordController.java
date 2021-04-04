package com.endeymus.planets.controllers;

import com.endeymus.planets.dao.LordDao;
import com.endeymus.planets.dao.PlanetDao;
import com.endeymus.planets.entities.Lord;
import com.endeymus.planets.entities.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lords")
public class LordController {

    private LordDao lordDao;
    private PlanetDao planetDao;
    @Autowired
    public void setLordDao(LordDao lordDao) {
        this.lordDao = lordDao;
    }
    @Autowired
    public void setPlanetDao(PlanetDao planetDao) {
        this.planetDao = planetDao;
    }

    @GetMapping("")
    public String listLords(Model model) {
        model.addAttribute("lords", lordDao.findAllWithPlanets());
        return "lords/lords";
    }

    @GetMapping("/lounger")
    public String lounger(Model model) {
        model.addAttribute("loungers", lordDao.findLounger());
        return "lords/lounger";
    }

    @GetMapping("/young")
    public String yound(Model model) {
        model.addAttribute("youngers", lordDao.findYoung());
        return "lords/young";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("lord", new Lord());
        return "lords/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("lord") Lord lord, Model model, RedirectAttributes attributes) {
        model.addAttribute("lord", lord);
        lordDao.insert(lord);
        return "redirect:lords/add";
    }

    @GetMapping("/appoint")
    public String appoint(Model model) {
        model.addAttribute("appoint", new Planet());
        model.addAttribute("lords", lordDao.findAll());
        model.addAttribute("planets", planetDao.findAll());
        return "lords/appoint";
    }

    @PostMapping("/appoint")
    public String appoint(@ModelAttribute Planet appoint) {
        Planet planet = planetDao.findById(appoint.getId());
        planet.setIdLord(appoint.getIdLord());
        planetDao.save(planet);
        return "redirect:/lords/appoint";
    }

}
