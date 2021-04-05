package com.endeymus.planets.controllers;

import com.endeymus.planets.dao.LordDao;
import com.endeymus.planets.dao.PlanetDao;
import com.endeymus.planets.entities.Lord;
import com.endeymus.planets.entities.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Класс рест контроллер, для API запросов
 * @author Mark Shamray
 */
@RestController
@RequestMapping("/api")
public class ApiController {

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

    /**
     * Выводит список всех планет
     * @return список планет
     */
    @GetMapping("/planets")
    public List<Planet> planetList() {
        return planetDao.findAll();
    }

    /**
     * Выводит список всех повелителей
     * @return список повелителей
     */
    @GetMapping("/lords")
    public List<Lord> lordList() {
        return lordDao.findAllWithPlanets();
    }

    /**
     * Добавление нового повелителя
     * @param name имя повелителя
     * @param age возраст повелителя
     * @return слово "успех" при удачном выполнеии
     */
    @PostMapping("/lords/add")
    public String addLord(@RequestParam String name, @RequestParam Integer age) {
        if (name == null || name.equals("") ||  age <= 0){
            return "failed to add new lord";
        }
        Lord lord = new Lord(name, age);
        lordDao.insert(lord);
        return "success";
    }

    /**
     * Добавление новой планеты
     * @param name название планеты
     * @return слово "успех" при удачном выполнеии
     */
    @PostMapping("/planets/add")
    public String addPlanet(@RequestParam String name) {
        if (name == null || name.equals("")) {
            return "failed to add new planet";
        }
        Planet planet = new Planet();
        planet.setName(name);
        planetDao.insert(planet);
        return "success";
    }


    /**
     * Назначение Повелителя управление планетой
     * @param lordId идентификатор Повелителя
     * @param planetId идентификатор Планеты
     * @return слово "успех" при удачном выполнеии
     */
    @PostMapping("/lords/appoint")
    public String appointLord(@RequestParam Integer lordId, @RequestParam Integer planetId) {
        Planet planet = planetDao.findById(planetId);
        if (planet == null) {
            return "failed to appoint lord";
        }
        planet.setIdLord(lordId);
        planetDao.save(planet);
        return "success";
    }

    /**
     * Уничтожение планеты
     * @param id идентификатор планеты
     * @return слово "успех" при удачном выполнеии
     */
    @GetMapping("/planet/{id}/delete")
    public String deletePlanet(@PathVariable Integer id) {
        Planet deletePlanet = planetDao.findById(id);
        if (deletePlanet == null) {
            return "failed to destroy planet";
        }
        planetDao.delete(deletePlanet);
        return "success";
    }

    /**
     * Поиск Повелителей бездельников, которые не управляют никакими планетами
     * @return список Повелителей бездельников
     */
    @GetMapping("/lords/loungers")
    public List<Lord> showLounger() {
        return lordDao.findLounger();
    }

    /**
     * Поиск ТОП 10 самых молодых повелителей
     * @return список повелителей
     */
    @GetMapping("/lords/young")
    public List<Lord> showYoung() {
        return lordDao.findYoung();
    }

}
