package ru.anotherworld.server.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.service.BuildingService;
import ru.anotherworld.server.rest.model.BuildingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Controller
@RequestMapping("/buildings")
@RequiredArgsConstructor
public class BuildingWebController {

    private final BuildingService buildingService;

    @GetMapping
    public String listBuildings(Model model) {
        model.addAttribute("buildings", buildingService.listAll());
        return "buildings";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("building", new BuildingDTO());
        return "building-form";
    }

    @PostMapping("/add")
    public String addBuilding(@Validated @ModelAttribute("building") BuildingDTO building, 
                            BindingResult result) {
        if (result.hasErrors()) {
            return "building-form";
        }
        buildingService.add(building.getName(), building.getCode());
        return "redirect:/buildings";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        BuildingDTO building = buildingService.findById(id);
        if (building != null) {
            model.addAttribute("building", building);
            return "building-form";
        }
        return "redirect:/buildings";
    }

    @PostMapping("/edit")
    public String updateBuilding(@Validated @ModelAttribute("building") BuildingDTO building,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "building-form";
        }
        buildingService.update(building.getId(), building.getName(), building.getCode());
        return "redirect:/buildings";
    }

    @PostMapping("/delete/{id}")
    public String deleteBuilding(@PathVariable Integer id) {
        buildingService.delete(id);
        return "redirect:/buildings";
    }
}
