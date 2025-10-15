package ru.anotherworld.server.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.rest.model.WaterSupplyDTO;
import ru.anotherworld.server.service.WaterSupplyService;
import ru.anotherworld.server.service.ApartmentService;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/watersupply")
@RequiredArgsConstructor
public class WaterSupplyWebController {

    private final WaterSupplyService waterSupplyService;
    private final ApartmentService apartmentService;

    @GetMapping
    public String listWaterSupply(Model model) {
        model.addAttribute("waterSupplyList", waterSupplyService.listAll());
        model.addAttribute("apartments", apartmentService.listAll());
        return "watersupply-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("waterSupply", new WaterSupplyDTO());
        model.addAttribute("apartments", apartmentService.listAll());
        return "watersupply-form";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addWaterSupply(
            @Validated @RequestBody WaterSupplyDTO waterSupply,
            BindingResult result) {
        
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            response.put("success", false);
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            waterSupplyService.add(
                waterSupply.getCold(),
                waterSupply.getHot(),
                waterSupply.getDebt() != null ? waterSupply.getDebt() : 0f,
                waterSupply.getActive() != null ? waterSupply.getActive() : false,
                waterSupply.getId()
            );
            response.put("success", true);
            response.put("message", "Water supply record added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding water supply record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        WaterSupplyDTO waterSupply = waterSupplyService.findById(id);
        if (waterSupply != null) {
            model.addAttribute("waterSupply", waterSupply);
            return "watersupply-form";
        }
        return "redirect:/watersupply";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> updateWaterSupply(
            @Validated @RequestBody WaterSupplyDTO waterSupply,
            BindingResult result) {
        
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            response.put("success", false);
            response.put("errors", result.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            waterSupplyService.update(
                waterSupply.getId(),
                waterSupply.getCold(),
                waterSupply.getHot(),
                waterSupply.getDebt() != null ? waterSupply.getDebt() : 0f,
                waterSupply.getActive() != null ? waterSupply.getActive() : false
            );
            response.put("success", true);
            response.put("message", "Water supply record updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating water supply record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteWaterSupply(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            waterSupplyService.delete(id);
            response.put("success", true);
            response.put("message", "Water supply record deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting water supply record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
