package sk.silvia.projects.iassesment.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sk.silvia.projects.iassesment.dao.UserRepository;
import sk.silvia.projects.iassesment.dto.CredentialsDTO;
import sk.silvia.projects.iassesment.entity.MyUser;
import sk.silvia.projects.iassesment.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/")
    public String blank() {
        return "home";
    }

    @GetMapping("/schedule/home")
    public String viewHome() {
        return "home";
    }

    @GetMapping("/changeLogin")
    public String changeLoginView(Model model) {
        model.addAttribute("credentialsDTO", new CredentialsDTO());
        boolean valid = true;
        model.addAttribute("valid", valid);
        return "changeLogin";
    }

    @PostMapping("/changeLogin")
    public String changeLoginCredentials(@ModelAttribute CredentialsDTO credentialsDTO, Model model) {
        boolean valid = userService.changeLoginCredentials(credentialsDTO.getOldUsername(), credentialsDTO.getNewUsername(), credentialsDTO.getOldPassword(), credentialsDTO.getNewPassword());
        if (valid)
            return "loginChanged";
        else {
            model.addAttribute("valid", valid);
            return "changeLogin";
        }
    }

    @GetMapping("/loginChanged")
    public String changedLoginView() {
        return "loginChanged";
    }



    /*@PostMapping("/login")
    public String logInPost(@ModelAttribute MyUser myUser) {
        if (userService.authentificateUser(myUser.getUsername(), myUser.getPassword()))
            return "home";
        else
            return "redirect:/login";
        return "home";

    @GetMapping("/signup")
    public String signupView(Model model) {
        model.addAttribute("user", new MyUser());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupView(Model model, @ModelAttribute MyUser myUser) {
        userService.registerUser(myUser.getUsername(), myUser.getPassword());
        return "signup";
    }
    }*/

}
