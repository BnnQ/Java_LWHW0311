package me.bnnq.homework.Controllers;

import lombok.AllArgsConstructor;
import me.bnnq.homework.Models.Rental;
import me.bnnq.homework.Repositories.IApartmentRepository;
import me.bnnq.homework.Repositories.IClientRepository;
import me.bnnq.homework.Repositories.IRentalRepository;
import me.bnnq.homework.Services.DataManagers.RentalManager;
import me.bnnq.homework.Utils.Views;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

@Controller
@AllArgsConstructor
public class RentalController
{
    private final IRentalRepository rentalRepository;
    private final IClientRepository clientRepository;
    private final IApartmentRepository apartmentRepository;
    private final RentalManager rentalManager;

    // ---------------GET---------------

    @GetMapping("/rentals")
    public String getRentals(Model model)
    {
        var rentals = rentalRepository.findAll();
        model.addAttribute("rentals", rentals);
        return Views.getView(model, "rentalList");
    }

    @GetMapping("/rental/create")
    public String createRental(Model model)
    {
        var clients = clientRepository.findByRentalIsNull();
        model.addAttribute("clients", clients);

        var apartments = apartmentRepository.findByRentalIsNull();
        model.addAttribute("apartments", apartments);

        return Views.getView(model, "rentalCreate");
    }

    @GetMapping("/rental/edit/{id}")
    public String editRental(Model model, @PathVariable(name = "id") Long id)
    {
        var rental = rentalRepository.findById(id).orElseThrow();
        model.addAttribute("rental", rental);

        var clients = clientRepository.findByRentalIsNull();
        model.addAttribute("clients", clients);

        var apartments = apartmentRepository.findByRentalIsNull();
        model.addAttribute("apartments", apartments);

        return Views.getView(model, "rentalEdit");
    }

    @GetMapping("/rental/delete/{id}")
    public String deleteRental(@PathVariable(name = "id") Long id)
    {
        var rental = rentalRepository.findById(id).orElseThrow();
        rentalManager.detachClient(rental);
        rentalManager.detachApartment(rental);
        rentalRepository.delete(rental);

        return "redirect:/rentals";
    }

    // ---------------GET---------------

    // ---------------POST---------------

    @PostMapping("/rental/create")
    public String createRental(@RequestParam(name = "clientId", required = false, defaultValue = "0") Long clientId, @RequestParam(name = "apartmentId", required = false, defaultValue = "0") Long apartmentId, @RequestParam(name = "rentStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date rentStartDate, @RequestParam(name = "rentEndDate") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date rentEndDate)
    {
        var rental = new Rental();
        rental.setRentStartDate(new Date(rentStartDate.getTime()));
        rental.setRentEndDate(new Date(rentEndDate.getTime()));
        if (clientId != 0)
        {
            var client = clientRepository.findById(clientId).orElseThrow();
            rental.setClient(client);

            if (apartmentId != 0)
            {
                var apartment = apartmentRepository.findById(apartmentId).orElseThrow();
                rental.setApartment(apartment);
            }
        }

        rentalRepository.save(rental);
        return "redirect:/rentals";
    }

    @PostMapping("/rental/edit")
    public String editRental(@RequestParam(name = "id") Long
                                     id, @RequestParam(name = "clientId", required = false, defaultValue = "0") Long
                                     clientId, @RequestParam(name = "apartmentId", required = false, defaultValue = "0") Long
                                     apartmentId, @RequestParam(name = "rentStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date
                                     rentStartDate, @RequestParam(name = "rentEndDate") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date rentEndDate)
    {
        var rental = rentalRepository.findById(id).orElseThrow();
        rental.setRentStartDate(new Date(rentStartDate.getTime()));
        rental.setRentEndDate(new Date(rentEndDate.getTime()));

        if (clientId != 0 && (rental.getClient() == null || !clientId.equals(rental.getClient().getId())))
        {
            var client = clientRepository.findById(clientId).orElseThrow();
            rental.setClient(client);
        }

        if (apartmentId != 0 && (rental.getApartment() == null || !apartmentId.equals(rental.getApartment().getId())))
        {
            var apartment = apartmentRepository.findById(apartmentId).orElseThrow();
            rental.setApartment(apartment);
        }

        rentalRepository.save(rental);
        return "redirect:/rentals";
    }

    // ---------------POST---------------
}