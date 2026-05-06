package rest_controller.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import rest_controller.model.User;
import rest_controller.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView adminPanel(Authentication authentication) {
        return new ModelAndView("admin", "admin", userService.getAllUsers())
                .addObject("user", userService.findByUsername(authentication.getName()));
    }

    @PostMapping("/add")
    public ModelAndView addUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return new ModelAndView("redirect:/admin");
    }

    @PostMapping("/delete")
    public ModelAndView deleteUser(@RequestParam("id") long id) {
        userService.deleteUser(id);
        return new ModelAndView("redirect:/admin");
    }

    @PostMapping("/update")
    public ModelAndView updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping("/new")
    public ModelAndView newUser() {
        return new ModelAndView("user-create", "user",new User());
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editUser(@PathVariable("id") long id) {
    return new ModelAndView("user-update", "user", userService.getUserById(id));
    }
}