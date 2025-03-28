package io.github.waspstdnt.wishlist_app.controllers;

import io.github.waspstdnt.wishlist_app.dtos.WishlistDto;
import io.github.waspstdnt.wishlist_app.exceptions.WishlistNotFoundException;
import io.github.waspstdnt.wishlist_app.models.User;
import io.github.waspstdnt.wishlist_app.models.Wishlist;
import io.github.waspstdnt.wishlist_app.services.TemplateService;
import io.github.waspstdnt.wishlist_app.services.WishlistService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final WishlistService wishlistService;
    private final TemplateService templateService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("wishlists", wishlistService.getWishlistsByUser(currentUser));
        return "dashboard";
    }

    @GetMapping("/lists/new")
    public String showListCreationForm(Model model) {
        model.addAttribute("wishlistDto", new WishlistDto());
        model.addAttribute("templates", templateService.getAllTemplates());
        return "list_create";
    }

    @PostMapping("/lists")
    public String createList(@Valid @ModelAttribute("wishlistDto") WishlistDto wishlistDto,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorDetails", bindingResult.getAllErrors());
            model.addAttribute("templates", templateService.getAllTemplates());
            return "list_create";
        }
        wishlistService.createWishlist(wishlistDto, currentUser);
        return "redirect:/dashboard";
    }

    @GetMapping("/lists/{id}")
    public String viewList(@PathVariable("id") Long id,
                           HttpSession session,
                           Model model) throws WishlistNotFoundException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        Wishlist wishlist = wishlistService.getWishlistById(id, currentUser);
        model.addAttribute("wishlist", wishlist);
        return "list_view";
    }

//    @GetMapping("/lists/{id}/edit")
//    public String editList(@PathVariable("id") Long id,
//                           HttpSession session,
//                           Model model) throws WishlistNotFoundException {
//        User currentUser = (User) session.getAttribute("currentUser");
//        if (currentUser == null) {
//            return "redirect:/login";
//        }
//        Wishlist wishlist = wishlistService.getWishlistById(id, currentUser);
//        model.addAttribute("wishlist", wishlist);
//
//        Long templateId = wishlist.getTemplate() != null ? wishlist.getTemplate().getId() : null;
//        model.addAttribute("wishlistDto", new WishlistDto(wishlist.getTitle(), templateId));
//        return "list_edit";
//    }
//
//    @PostMapping("/lists/{id}/update")
//    public String updateList(@PathVariable("id") Long id,
//                             @Valid @ModelAttribute("wishlistDto") WishlistDto wishlistDto,
//                             BindingResult bindingResult,
//                             HttpSession session,
//                             Model model) throws WishlistNotFoundException {
//        User currentUser = (User) session.getAttribute("currentUser");
//        if (currentUser == null) {
//            return "redirect:/login";
//        }
//        if (bindingResult.hasErrors()) {
//            Wishlist wishlist = wishlistService.getWishlistById(id, currentUser);
//            model.addAttribute("wishlist", wishlist);
//            return "list_edit";
//        }
//        wishlistService.updateWishlist(id, wishlistDto, currentUser);
//        return "redirect:/lists/" + id;
//    }

    @PostMapping("/lists/{id}/delete")
    public String deleteList(@PathVariable("id") Long id, HttpSession session) throws WishlistNotFoundException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        wishlistService.deleteWishlist(id, currentUser);
        return "redirect:/dashboard";
    }
}
