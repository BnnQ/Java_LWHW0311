package me.bnnq.homework.Controllers;

import lombok.RequiredArgsConstructor;
import me.bnnq.homework.Repositories.IRentalRepository;
import me.bnnq.homework.Utils.Views;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class QueryController
{
    private final IRentalRepository rentalRepository;

    @GetMapping("/query")
    public String getQuery(Model model, @RequestParam(name = "q") String query)
    {
        model.addAttribute("query", query);

        switch (query)
        {
            case "averageRentalDuration":
            {
                model.addAttribute("averageRentalDuration", rentalRepository.findAverageRentalDuration());
                return Views.getView(model, "queryResult", "Average rental duration");
            }
            case "minimumRentalDuration":
            {
                model.addAttribute("minimumRentalDuration", rentalRepository.findMinimumRentalDuration());
                return Views.getView(model, "queryResult", "Minimum rental duration");
            }
            case "maximumRentalDuration":
            {
                model.addAttribute("maximumRentalDuration", rentalRepository.findMaximumRentalDuration());
                return Views.getView(model, "queryResult", "Maximum rental duration");
            }
            default:
            {
                return "redirect:/rentals";
            }
        }
    }
}