package me.bnnq.homework.Controllers;

import me.bnnq.homework.Utils.Views;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController
{
    @GetMapping("/")
    public String getMenu(Model model)
    {
        return Views.getView(model, "menu");
    }
}