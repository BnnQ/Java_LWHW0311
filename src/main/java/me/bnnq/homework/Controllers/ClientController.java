package me.bnnq.homework.Controllers;

import lombok.AllArgsConstructor;
import me.bnnq.homework.Models.Client;
import me.bnnq.homework.Models.Rental;
import me.bnnq.homework.Repositories.IClientRepository;
import me.bnnq.homework.Repositories.IRentalRepository;
import me.bnnq.homework.Services.DataManagers.ClientManager;
import me.bnnq.homework.Utils.Strings;
import me.bnnq.homework.Utils.Views;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@AllArgsConstructor
public class ClientController
{
    private final IClientRepository clientRepository;
    private final IRentalRepository rentalRepository;
    private final ClientManager clientManager;

    // ---------------GET---------------

    @GetMapping("/clients")
    public String getClients(@RequestParam(required = false) String firstName,
                             @RequestParam(required = false) String lastName,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) Boolean rentCurrentMonth,
                             @RequestParam(required = false) Boolean rentEndsThisMonth,
                             @RequestParam(required = false) Boolean rentLessThanMonth,
                             @RequestParam(required = false) Boolean rentMoreThanYear,
                             Model model)
    {
        var clients = clientRepository.findAll();

        if (!Strings.isNullOrEmpty(firstName))
        {
            clients = StreamSupport.stream(clients.spliterator(), false)
                    .filter(client -> client.getFirstName().equals(firstName))
                    .collect(Collectors.toList());
        }

        if (!Strings.isNullOrEmpty(lastName))
        {
            clients = StreamSupport.stream(clients.spliterator(), false)
                    .filter(client -> client.getLastName().equals(lastName))
                    .collect(Collectors.toList());
        }

        if (!Strings.isNullOrEmpty(email))
        {
            clients = StreamSupport.stream(clients.spliterator(), false)
                    .filter(client -> client.getEmail().equals(email))
                    .collect(Collectors.toList());
        }

        if (rentCurrentMonth != null && rentCurrentMonth)
        {
            clients = StreamSupport.stream(clients.spliterator(), false)
                    .filter(client ->
                    {
                        Rental rental = client.getRental();
                        if (rental != null)
                        {
                            var now = LocalDate.now();
                            var rentStartDate = rental.getRentStartDate().toLocalDate();
                            return rentStartDate.getYear() == now.getYear() && rentStartDate.getMonth().getValue() == now.getMonth().getValue();
                        }

                        return false;
                    })
                    .collect(Collectors.toList());
        }

        if (rentEndsThisMonth != null && rentEndsThisMonth)
        {
            clients = StreamSupport.stream(clients.spliterator(), false)
                    .filter(client ->
                    {
                        Rental rental = client.getRental();
                        if (rental != null)
                        {
                            var now = LocalDate.now();
                            var rentEndDate = rental.getRentEndDate().toLocalDate();
                            return rentEndDate.getYear() == now.getYear() && rentEndDate.getMonth().getValue() == now.getMonth().getValue();
                        }

                        return false;
                    })
                    .collect(Collectors.toList());
        }

        if (rentLessThanMonth != null && rentLessThanMonth)
        {
            clients = StreamSupport.stream(clients.spliterator(), false)
                    .filter(client ->
                    {
                        Rental rental = client.getRental();
                        if (rental != null)
                        {
                            LocalDate startDate = rental.getRentStartDate().toLocalDate();
                            LocalDate endDate = rental.getRentEndDate().toLocalDate();
                            return ChronoUnit.DAYS.between(startDate, endDate) < 30;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        if (rentMoreThanYear != null && rentMoreThanYear)
        {
            clients = StreamSupport.stream(clients.spliterator(), false)
                    .filter(client ->
                    {
                        Rental rental = client.getRental();
                        if (rental != null)
                        {
                            LocalDate startDate = rental.getRentStartDate().toLocalDate();
                            LocalDate endDate = rental.getRentEndDate().toLocalDate();
                            return ChronoUnit.DAYS.between(startDate, endDate) > 365;
                        }

                        return false;
                    })
                    .collect(Collectors.toList());
        }

        model.addAttribute("clients", clients);
        return Views.getView(model, "clientList");
    }


    @GetMapping("/client/create")
    public String createClient(Model model)
    {
        var rentals = rentalRepository.findByClientIsNull();
        model.addAttribute("rentals", rentals);

        return Views.getView(model, "clientCreate");
    }

    @GetMapping("/client/edit/{id}")
    public String editClient(Model model, @PathVariable(name = "id") Long id)
    {
        var client = clientRepository.findById(id).orElseThrow();
        model.addAttribute("client", client);

        var rentals = rentalRepository.findByClientIsNull();
        model.addAttribute("rentals", rentals);

        return Views.getView(model, "clientEdit");
    }

    @GetMapping("/client/delete/{id}")
    public String deleteClient(@PathVariable(name = "id") Long id)
    {
        var client = clientRepository.findById(id).orElseThrow();
        clientManager.detachRental(client);
        clientRepository.delete(client);

        return "redirect:/clients";
    }

    // ---------------GET---------------

    // ---------------POST---------------

    @PostMapping("/client/create")
    public String createClient(@RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "email") String email)
    {
        var client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);

        clientRepository.save(client);
        return "redirect:/clients";
    }

    @PostMapping("/client/edit")
    public String editClient(@RequestParam(name = "id") Long id, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(name = "email") String email)
    {
        var client = clientRepository.findById(id).orElseThrow();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);

        clientRepository.save(client);
        return "redirect:/clients";
    }

    // ---------------POST---------------

}