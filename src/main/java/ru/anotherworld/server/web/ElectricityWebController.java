package ru.anotherworld.server.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.rest.model.ElectricityDTO;
import ru.anotherworld.server.service.ElectricityService;
import ru.anotherworld.server.service.ApartmentService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/electricity")
@RequiredArgsConstructor
public class ElectricityWebController {

    private final ElectricityService electricityService;
    private final ApartmentService apartmentService;

    @GetMapping
    public String listElectricity(Model model) {
        model.addAttribute("electricityList", electricityService.listAll());
        model.addAttribute("apartments", apartmentService.listAll());
        return "electricity-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("electricity", new ElectricityDTO());
        model.addAttribute("apartments", apartmentService.listAll());
        return "electricity-form";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addElectricity(
            @Validated @RequestBody ElectricityDTO electricity,
            BindingResult result) {
        
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            response.put("success", false);
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            electricityService.add(
                electricity.getDay(),
                electricity.getNight(),
                electricity.getDebt() != null ? electricity.getDebt() : 0f,
                electricity.getActive() != null ? electricity.getActive() : false,
                electricity.getId()
            );
            response.put("success", true);
            response.put("message", "Electricity record added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding electricity record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        ElectricityDTO electricity = electricityService.findById(id);
        if (electricity != null) {
            model.addAttribute("electricity", electricity);
            model.addAttribute("apartments", apartmentService.listAll());
            return "electricity-form";
        }
        return "redirect:/electricity";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> updateElectricity(
            @Validated @RequestBody ElectricityDTO electricity,
            BindingResult result) {
        
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            response.put("success", false);
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            electricityService.update(
                electricity.getId(),
                electricity.getDay(),
                electricity.getNight(),
                electricity.getDebt() != null ? electricity.getDebt() : 0f,
                electricity.getActive() != null ? electricity.getActive() : false
            );
            response.put("success", true);
            response.put("message", "Electricity record updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating electricity record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteElectricity(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            electricityService.delete(id);
            response.put("success", true);
            response.put("message", "Electricity record deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting electricity record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
