package com.endeymus.planets.controllers;

import com.endeymus.planets.dao.LordDao;
import com.endeymus.planets.dao.PlanetDao;
import com.endeymus.planets.entities.Lord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Класс контроллер, для обработки запросов на стартовой странице
 * @author Mark Shamray
 */
@Controller
@RequestMapping("")
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
