package managerapp.controllers;


import managerapp.models.OldUser;
import managerapp.models.UserData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model, OldUser oldUser) {
        model.addAttribute("title", "Sign Up");
        return "oldUser/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid OldUser oldUser, BindingResult bindingResult) {
        model.addAttribute("title", "Sign Up");


        // TODO: Make email optional

        if (bindingResult.hasErrors()) {
            return "/oldUser/add";
        }

        UserData.add(oldUser);
        String name = oldUser.getUserName();
        return String.format("redirect:/oldUser/index/%s", name);
    }

    @RequestMapping(value = "index")
    public String index(Model model) {
        model.addAttribute("title", "Message Users");
        ArrayList users = UserData.getAll();
        model.addAttribute("users", users);
        return "/user/index";
    }

    @RequestMapping(value = "index/{name}")
    public String index(Model model, @PathVariable String name) {
        model.addAttribute("title", name);
        model.addAttribute("name", name);
        return "/user/index";
    }

}
