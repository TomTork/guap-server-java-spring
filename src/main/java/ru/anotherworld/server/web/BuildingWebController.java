package ru.anotherworld.server.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.service.BuildingService;
import ru.anotherworld.server.rest.model.BuildingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import java.util.HashMap;
import java.util.Map;

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
    @ResponseBody
    public ResponseEntity<?> addBuilding(@Validated @RequestBody BuildingDTO building, 
                                      BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            response.put("success", false);
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            buildingService.add(building.getName(), building.getCode());
            response.put("success", true);
            response.put("message", "Building added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding building: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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
    @ResponseBody
    public ResponseEntity<?> updateBuilding(@Validated @RequestBody BuildingDTO building,
                                         BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            response.put("success", false);
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            buildingService.update(building.getId(), building.getName(), building.getCode());
            response.put("success", true);
            response.put("message", "Building updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating building: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteBuilding(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            buildingService.delete(id);
            response.put("success", true);
            response.put("message", "Building deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting building: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
