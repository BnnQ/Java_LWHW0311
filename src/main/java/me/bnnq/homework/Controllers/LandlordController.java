package me.bnnq.homework.Controllers;

import lombok.AllArgsConstructor;
import me.bnnq.homework.Models.Landlord;
import me.bnnq.homework.Repositories.IApartmentRepository;
import me.bnnq.homework.Repositories.ILandlordRepository;
import me.bnnq.homework.Services.DataManagers.LandlordManager;
import me.bnnq.homework.Utils.Views;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@AllArgsConstructor
public class LandlordController
{
    private final ILandlordRepository landlordRepository;
    private final IApartmentRepository apartmentRepository;
    private final LandlordManager landlordManager;

    // ---------------GET---------------

    @GetMapping("/landlords")
    public String getLandlords(Model model)
    {
        var landlords = landlordRepository.findAll();
        model.addAttribute("landlords", landlords);
        return Views.getView(model, "landlordList");
    }

    @GetMapping("/landlord/create")
    public String createLandlord(Model model)
    {
        var apartments = apartmentRepository.findByLandlordIsNull();
        model.addAttribute("apartments", apartments);

        return Views.getView(model, "landlordCreate");
    }

    @GetMapping("/landlord/edit/{id}")
    public String editLandlord(Model model, @PathVariable(name = "id") Long id)
    {
        var landlord = landlordRepository.findById(id).orElseThrow();
        model.addAttribute("landlord", landlord);

        var apartments = apartmentRepository.findByLandlordIsNull();
        apartments.addAll(landlord.getApartments());
        model.addAttribute("apartments", apartments);

        return Views.getView(model, "landlordEdit");
    }

    @GetMapping("/landlord/delete/{id}")
    public String deleteLandlord(@PathVariable(name = "id") Long id)
    {
        var landlord = landlordRepository.findById(id).orElseThrow();
        landlordManager.detachApartments(landlord);
        landlordRepository.delete(landlord);

        return "redirect:/landlords";
    }

    // ---------------GET---------------

    // ---------------POST---------------

    @PostMapping("/landlord/create")
    public String createLandlord(@RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "email") String email, @RequestParam(name = "apartmentIds", required = false) Long[] apartmentIds)
    {
        var landlord = new Landlord();
        landlord.setFirstName(firstName);
        landlord.setLastName(lastName);
        landlord.setEmail(email);

        if (apartmentIds != null && apartmentIds.length > 0 && Arrays.stream(apartmentIds).noneMatch(id -> id == 0))
        {
            var apartments = apartmentRepository.findAllById(Arrays.asList(apartmentIds));
            landlord.setApartments(StreamSupport.stream(apartments.spliterator(), false).toList());
        }

        landlordRepository.save(landlord);
        return "redirect:/landlords";
    }

    @PostMapping("/landlord/edit")
    public String editLandlord(@RequestParam(name = "id") Long id, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "email") String email, @RequestParam(name = "apartmentIds", required = false) Long[] apartmentIds)
    {
        var landlord = landlordRepository.findById(id).orElseThrow();
        landlord.setFirstName(firstName);
        landlord.setLastName(lastName);
        landlord.setEmail(email);

        if (apartmentIds != null && apartmentIds.length > 0)
        {
            landlordManager.detachApartments(landlord);
            if (Arrays.stream(apartmentIds).noneMatch(_id -> _id == 0))
            {
                var apartments = apartmentRepository.findAllById(Arrays.asList(apartmentIds));
                landlord.setApartments(StreamSupport.stream(apartments.spliterator(), false).collect(Collectors.toList()));

                apartments.forEach(apartment ->
                {
                    apartment.setLandlord(landlord);
                    apartmentRepository.save(apartment);
                });

            }
        }

        landlordRepository.save(landlord);
        return "redirect:/landlords";
    }

    // ---------------POST---------------

}
