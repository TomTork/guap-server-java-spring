package ru.anotherworld.server.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.rest.model.WaterSupplyDTO;
import ru.anotherworld.server.service.WaterSupplyService;
import ru.anotherworld.server.service.ApartmentService;

@Controller
@RequestMapping("/watersupply")
@RequiredArgsConstructor
public class WaterSupplyWebController {

    private final WaterSupplyService waterSupplyService;
    private final ApartmentService apartmentService;

    @GetMapping
    public String listWaterSupply(Model model) {
        model.addAttribute("waterSupplyList", waterSupplyService.listAll());
        return "watersupply-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("waterSupply", new WaterSupplyDTO());
        model.addAttribute("apartments", apartmentService.listAll());
        return "watersupply-form";
    }

    @PostMapping("/add")
    public String addWaterSupply(
            @Validated @ModelAttribute("waterSupply") WaterSupplyDTO waterSupply,
            BindingResult result,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("apartments", apartmentService.listAll());
            return "watersupply-form";
        }
        
        waterSupplyService.add(
            waterSupply.getCold(),
            waterSupply.getHot(),
            waterSupply.getDebt() != null ? waterSupply.getDebt() : 0f,
            waterSupply.getActive() != null ? waterSupply.getActive() : false,
            waterSupply.getId()
        );
        return "redirect:/watersupply";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        WaterSupplyDTO waterSupply = waterSupplyService.findById(id);
        if (waterSupply != null) {
            model.addAttribute("waterSupply", waterSupply);
            model.addAttribute("apartments", apartmentService.listAll());
            return "watersupply-form";
        }
        return "redirect:/watersupply";
    }

    @PostMapping("/edit")
    public String updateWaterSupply(
            @Validated @ModelAttribute("waterSupply") WaterSupplyDTO waterSupply,
            BindingResult result,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("apartments", apartmentService.listAll());
            return "watersupply-form";
        }
        
        waterSupplyService.update(
            waterSupply.getId(),
            waterSupply.getCold(),
            waterSupply.getHot(),
            waterSupply.getDebt() != null ? waterSupply.getDebt() : 0f,
            waterSupply.getActive() != null ? waterSupply.getActive() : false
        );
        return "redirect:/watersupply";
    }

    @PostMapping("/delete/{id}")
    public String deleteWaterSupply(@PathVariable Integer id) {
        waterSupplyService.delete(id);
        return "redirect:/watersupply";
    }
}
