package ru.anotherworld.server.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.anotherworld.server.rest.model.ApartmentDTO;
import ru.anotherworld.server.service.ApartmentService;
import ru.anotherworld.server.service.BuildingService;

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
    public String addApartment(
            @Validated @ModelAttribute("apartment") ApartmentDTO apartment,
            BindingResult result,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("buildings", buildingService.listAll());
            return "apartment-form";
        }
        
        apartmentService.add(
            apartment.getNumber(),
            apartment.getTotalSquare(),
            apartment.getLivingSquare(),
            apartment.getRoomsAmount(),
            apartment.getFloor(),
            apartment.getBuildingId()
        );
        return "redirect:/apartments";
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
    public String updateApartment(
            @Validated @ModelAttribute("apartment") ApartmentDTO apartment,
            BindingResult result,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("buildings", buildingService.listAll());
            return "apartment-form";
        }
        
        apartmentService.update(
            apartment.getId(),
            apartment.getNumber(),
            apartment.getTotalSquare(),
            apartment.getLivingSquare(),
            apartment.getRoomsAmount(),
            apartment.getFloor(),
            apartment.getBuildingId()
        );
        return "redirect:/apartments";
    }

    @PostMapping("/delete/{id}")
    public String deleteApartment(@PathVariable Integer id) {
        apartmentService.delete(id);
        return "redirect:/apartments";
    }
}
