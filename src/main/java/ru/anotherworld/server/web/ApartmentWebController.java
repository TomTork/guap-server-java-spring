package ru.anotherworld.server.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.rest.model.ApartmentDTO;
import ru.anotherworld.server.service.ApartmentService;
import ru.anotherworld.server.service.BuildingService;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentWebController {

    private final ApartmentService apartmentService;
    private final BuildingService buildingService;

    @GetMapping
    public String listApartments(Model model) {
        model.addAttribute("apartments", apartmentService.listAll());
        return "apartments";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("apartment", new ApartmentDTO());
        model.addAttribute("buildings", buildingService.listAll());
        return "apartment-form";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addApartment(
            @Validated @RequestBody ApartmentDTO apartment,
            BindingResult result) {
        
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            response.put("success", false);
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            apartmentService.add(
                apartment.getNumber(),
                apartment.getTotalSquare(),
                apartment.getLivingSquare(),
                apartment.getRoomsAmount(),
                apartment.getFloor(),
                apartment.getBuildingId()
            );
            response.put("success", true);
            response.put("message", "Apartment added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding apartment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        ApartmentDTO apartment = apartmentService.findById(id);
        if (apartment != null) {
            model.addAttribute("apartment", apartment);
            model.addAttribute("buildings", buildingService.listAll());
            return "apartment-form";
        }
        return "redirect:/apartments";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> updateApartment(
            @Validated @RequestBody ApartmentDTO apartment,
            BindingResult result) {
        
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            response.put("success", false);
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            apartmentService.update(
                apartment.getId(),
                apartment.getNumber(),
                apartment.getTotalSquare(),
                apartment.getLivingSquare(),
                apartment.getRoomsAmount(),
                apartment.getFloor(),
                apartment.getBuildingId()
            );
            response.put("success", true);
            response.put("message", "Apartment updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating apartment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteApartment(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            apartmentService.delete(id);
            response.put("success", true);
            response.put("message", "Apartment deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting apartment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
