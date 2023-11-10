package me.bnnq.homework.Controllers;

import lombok.AllArgsConstructor;
import me.bnnq.homework.Models.Apartment;
import me.bnnq.homework.Repositories.IApartmentRepository;
import me.bnnq.homework.Repositories.ILandlordRepository;
import me.bnnq.homework.Services.DataManagers.ApartmentManager;
import me.bnnq.homework.Utils.Views;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class ApartmentController
{
    private final IApartmentRepository apartmentRepository;
    private final ILandlordRepository landlordRepository;
    private final ApartmentManager apartmentManager;

    // ---------------GET---------------

    @GetMapping("/apartments")
    public String getApartments(Model model)
    {
        var apartments = apartmentRepository.findAll();
        model.addAttribute("apartments", apartments);
        return Views.getView(model, "apartmentList");
    }

    @GetMapping("/apartment/create")
    public String createApartment(Model model)
    {
        var landlords = landlordRepository.findAll();
        model.addAttribute("landlords", landlords);

        return Views.getView(model, "apartmentCreate");
    }

    @GetMapping("/apartment/edit/{id}")
    public String editApartment(Model model, @PathVariable(name = "id") Long id)
    {
        var apartment = apartmentRepository.findById(id).orElseThrow();
        model.addAttribute("apartment", apartment);

        var landlords = landlordRepository.findAll();
        model.addAttribute("landlords", landlords);

        return Views.getView(model, "apartmentEdit");
    }

    @GetMapping("/apartment/delete/{id}")
    public String deleteApartment(@PathVariable(name = "id") Long id)
    {
        var apartment = apartmentRepository.findById(id).orElseThrow();
        apartmentManager.detachRental(apartment);
        apartmentRepository.delete(apartment);

        return "redirect:/apartments";
    }

    // ---------------GET---------------

    // ---------------POST---------------

    @PostMapping("/apartment/create")
    public String createApartment(@RequestParam(name = "address") String address, @RequestParam(name = "landlordId", required = false, defaultValue = "0") Long landlordId)
    {
        var apartment = new Apartment();
        apartment.setAddress(address);

        if (landlordId != 0)
        {
            var landlord = landlordRepository.findById(landlordId).orElseThrow();
            apartment.setLandlord(landlord);
        }

        apartmentRepository.save(apartment);
        return "redirect:/apartments";
    }

    @PostMapping("/apartment/edit")
    public String editApartment(@RequestParam(name = "id") Long id, @RequestParam(name = "address") String address, @RequestParam(name = "landlordId", required = false, defaultValue = "0") Long landlordId)
    {
        var apartment = apartmentRepository.findById(id).orElseThrow();
        apartment.setAddress(address);

        if (landlordId != 0 && (apartment.getLandlord() == null || !landlordId.equals(apartment.getLandlord().getId())))
        {
            var landlord = landlordRepository.findById(landlordId).orElseThrow();
            apartment.setLandlord(landlord);
        }

        apartmentRepository.save(apartment);
        return "redirect:/apartments";
    }

    // ---------------POST---------------
}
