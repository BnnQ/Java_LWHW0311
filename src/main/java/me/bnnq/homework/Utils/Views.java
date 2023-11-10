package me.bnnq.homework.Utils;

import org.springframework.ui.Model;

public class Views
{
    public static String getView(Model model, String name)
    {
        model.addAttribute("view", name);
        return "layout";
    }

    public static String getView(Model model, String name, String title)
    {
        model.addAttribute("title", title);
        return getView(model, name);
    }
}
