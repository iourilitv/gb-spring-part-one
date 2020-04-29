package ru.geekbrains.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.repository.RoleRepository;
import ru.geekbrains.exception.NotFoundException;
import ru.geekbrains.service.UserRepr;
import ru.geekbrains.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

//    @GetMapping
//    public String allUsers(Model model) {
//        model.addAttribute("activePage", "Users");
//        model.addAttribute("users", userService.findAll());
//        return "users";
//    }
    @GetMapping
    public String allUsers(@RequestParam(value = "page") Optional<Integer> page,
                           @RequestParam(value = "limit") Optional<Integer> limit,
                           Model model) {
        model.addAttribute("activePage", "Users");
        model.addAttribute("userPage", userService.findAll(
                PageRequest.of(page.orElse(1) - 1, limit.orElse(5))
        ));
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/{id}")
    public String editUser(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id).orElseThrow(NotFoundException::new));
        model.addAttribute("roles", roleRepository.findAll());
        return "user_form";
    }

    @GetMapping("/form")
    public String formUser(Model model) {
        model.addAttribute("user", new UserRepr());
        model.addAttribute("roles", roleRepository.findAll());
        return "user_form";
    }

    @PostMapping("/form")
    public String newUser(@Valid UserRepr user, BindingResult result) {
        if (result.hasErrors()) {
            return "user_form";
        }
        if (!user.getPassword().equals(user.getMatchingPassword())) {
            result.rejectValue("password", "", "Password not matching");
            return "user_form";
        }

        userService.save(user);
        return "redirect:/user";
    }
}
