package ru.geekbrains.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome(Model model) {
        //добавляем атрибут активной страницы со значением главной страницы
        //см. в header.html
        model.addAttribute("activePage", "IndexPage");
        model.addAttribute("helloText",
                "Добро пожаловать в наш замечательный магазин бытовой техники!");
        return "welcome";
    }

}
