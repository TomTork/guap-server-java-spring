package ru.anotherworld.server.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.rest.model.ElectricityDTO;
import ru.anotherworld.server.service.ElectricityService;
import ru.anotherworld.server.service.ApartmentService;

@Controller
@RequestMapping("/electricity")
@RequiredArgsConstructor
public class ElectricityWebController {

    private final ElectricityService electricityService;
    private final ApartmentService apartmentService;

    @GetMapping
    public String listElectricity(Model model) {
        model.addAttribute("electricityList", electricityService.listAll());
        return "electricity-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("electricity", new ElectricityDTO());
        model.addAttribute("apartments", apartmentService.listAll());
        return "electricity-form";
    }

    @PostMapping("/add")
    public String addElectricity(
            @Validated @ModelAttribute("electricity") ElectricityDTO electricity,
            BindingResult result,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("apartments", apartmentService.listAll());
            return "electricity-form";
        }
        
        electricityService.add(
            electricity.getDay(),
            electricity.getNight(),
            electricity.getDebt() != null ? electricity.getDebt() : 0f,
            electricity.getActive() != null ? electricity.getActive() : false,
            electricity.getId()
        );
        return "redirect:/electricity";
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
    public String updateElectricity(
            @Validated @ModelAttribute("electricity") ElectricityDTO electricity,
            BindingResult result,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("apartments", apartmentService.listAll());
            return "electricity-form";
        }
        
        electricityService.update(
            electricity.getId(),
            electricity.getDay(),
            electricity.getNight(),
            electricity.getDebt() != null ? electricity.getDebt() : 0f,
            electricity.getActive() != null ? electricity.getActive() : false
        );
        return "redirect:/electricity";
    }

    @PostMapping("/delete/{id}")
    public String deleteElectricity(@PathVariable Integer id) {
        electricityService.delete(id);
        return "redirect:/electricity";
    }
}
