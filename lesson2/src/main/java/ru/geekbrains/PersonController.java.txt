package ru.geekbrains;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.persist.Person;
import ru.geekbrains.persist.PersonRepository;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final PersonRepository personRepository;

    @Autowired
    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    public String allPersons(Model model) {
        model.addAttribute("persons", personRepository.getAllPersons());
        return "persons";
    }

    @GetMapping("/form")
    public String formPerson(Model model) {
        model.addAttribute("person", new Person());
        return "person_form";
    }

    @PostMapping("/form")
    public String newPerson(Person person) {
        personRepository.insert(person);
        return "redirect:/person";
    }
}
