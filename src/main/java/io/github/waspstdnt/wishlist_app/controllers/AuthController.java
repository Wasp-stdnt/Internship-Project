package io.github.waspstdnt.wishlist_app.controllers;

import io.github.waspstdnt.wishlist_app.dtos.UserRegistrationDto;
import io.github.waspstdnt.wishlist_app.exceptions.InvalidCredentialsException;
import io.github.waspstdnt.wishlist_app.exceptions.UserAlreadyExistsException;
import io.github.waspstdnt.wishlist_app.exceptions.UserNotFoundException;
import io.github.waspstdnt.wishlist_app.models.User;
import io.github.waspstdnt.wishlist_app.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public String landing() {
        return "auth/landing";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDto", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationDto") UserRegistrationDto registrationDto,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            User user = userService.registerUser(registrationDto);
            session.setAttribute("currentUser", user);
            return "redirect:/dashboard";
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("registrationError", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpSession session,
                            Model model) {
        try {
            User user = userService.authenticateUser(email, password);
            session.setAttribute("currentUser", user);
            return "redirect:/dashboard";
        } catch (InvalidCredentialsException | UserNotFoundException e) {
            model.addAttribute("loginError", e.getMessage());
            return "auth/login";
        }
    }

    @PostMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/account")
    public String accountSettings(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("registrationDto", new UserRegistrationDto(currentUser.getUsername(), currentUser.getEmail(), ""));
        return "auth/account";
    }

//    @PostMapping("/account/update")
//    public String updateAccount(@Valid @ModelAttribute("registrationDto") UserRegistrationDto registrationDto,
//                                BindingResult bindingResult,
//                                HttpSession session,
//                                Model model) {
//        User currentUser = (User) session.getAttribute("currentUser");
//        if (currentUser == null) {
//            return "redirect:/login";
//        }
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("currentUser", currentUser);
//            return "account";
//        }
//        try {
//            User updatedUser = userService.updateUser(currentUser.getId(), registrationDto);
//            session.setAttribute("currentUser", updatedUser);
//            model.addAttribute("updateSuccess", "Account updated successfully.");
//            return "account";
//        } catch (UserNotFoundException e) {
//            model.addAttribute("updateError", e.getMessage());
//            return "account";
//        }
//    }

    @PostMapping("/account/delete")
    public String deleteAccount(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            try {
                userService.deleteUser(currentUser.getId());
            } catch (UserNotFoundException e) {
                System.out.println("Encountered exception: " + e.getMessage());
            }
        }
        session.invalidate();
        return "redirect:/";
    }
}
