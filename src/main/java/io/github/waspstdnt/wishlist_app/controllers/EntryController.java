package io.github.waspstdnt.wishlist_app.controllers;

import io.github.waspstdnt.wishlist_app.dtos.EntryDto;
import io.github.waspstdnt.wishlist_app.exceptions.EntryNotFoundException;
import io.github.waspstdnt.wishlist_app.exceptions.InvalidEntryDataException;
import io.github.waspstdnt.wishlist_app.exceptions.WishlistNotFoundException;
import io.github.waspstdnt.wishlist_app.models.Entry;
import io.github.waspstdnt.wishlist_app.models.User;
import io.github.waspstdnt.wishlist_app.models.Wishlist;
import io.github.waspstdnt.wishlist_app.services.EntryService;
import io.github.waspstdnt.wishlist_app.services.WishlistService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;
    private final WishlistService wishlistService;

    @GetMapping("/lists/{listId}/entries/new")
    public String showEntryCreationForm(@PathVariable("listId") Long listId,
                                        Model model,
                                        HttpSession session) throws WishlistNotFoundException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Wishlist wishlist = wishlistService.getWishlistById(listId, currentUser);

        model.addAttribute("templateFields", wishlist.getTemplate().getTemplateFields());
        model.addAttribute("entryDto", new EntryDto());
        model.addAttribute("listId", listId);
        return "entry/entry_create";
    }

    @PostMapping("/lists/{listId}/entries")
    public String createEntry(@PathVariable("listId") Long listId,
                              @Valid @ModelAttribute("entryDto") EntryDto entryDto,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model) throws WishlistNotFoundException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            Wishlist wishlist = wishlistService.getWishlistById(listId, currentUser);
            model.addAttribute("templateFields", wishlist.getTemplate().getTemplateFields());
            model.addAttribute("listId", listId);
            return "entry/entry_create";
        }
        try {
            entryService.createEntry(listId, entryDto, currentUser);
            return "redirect:/lists/" + listId;
        } catch (InvalidEntryDataException e) {
            Wishlist wishlist = wishlistService.getWishlistById(listId, currentUser);
            model.addAttribute("templateFields", wishlist.getTemplate().getTemplateFields());
            model.addAttribute("creationError", e.getMessage());
            model.addAttribute("listId", listId);
            return "entry/entry_create";
        }
    }

    @GetMapping("/entries/{id}")
    public String viewEntry(@PathVariable("id") Long entryId, HttpSession session, Model model) throws EntryNotFoundException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        Entry entry = entryService.getEntryById(entryId, currentUser);
        model.addAttribute("entry", entry);

        Wishlist wishlist = entry.getWishlist();
        model.addAttribute("wishlist", wishlist);
        model.addAttribute("template", wishlist.getTemplate());
        model.addAttribute("templateFields", wishlist.getTemplate().getTemplateFields());

        return "entry/entry_view";
    }

    @GetMapping("/entries/{id}/edit")
    public String showEditEntryForm(@PathVariable("id") Long entryId, HttpSession session, Model model) throws EntryNotFoundException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Entry entry = entryService.getEntryById(entryId, currentUser);
        Wishlist wishlist = entry.getWishlist();
        model.addAttribute("wishlist", wishlist);
        model.addAttribute("templateFields", wishlist.getTemplate().getTemplateFields());

        EntryDto entryDto = EntryDto.builder()
                .title(entry.getTitle())
                .data(entry.getData())
                .build();
        model.addAttribute("entryDto", entryDto);
        model.addAttribute("entryId", entryId);
        return "entry/entry_edit";
    }

    @PostMapping("/entries/{id}/update")
    public String updateEntry(@PathVariable("id") Long entryId,
                              @Valid @ModelAttribute("entryDto") EntryDto entryDto,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model) throws EntryNotFoundException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("entryId", entryId);
            return "entry/entry_edit";
        }
        try {
            entryService.updateEntry(entryId, entryDto, currentUser);
            return "redirect:/entries/" + entryId;
        } catch (InvalidEntryDataException e) {
            model.addAttribute("updateError", e.getMessage());
            model.addAttribute("entryId", entryId);
            return "entry/entry_edit";
        }
    }

    @PostMapping("/entries/{id}/delete")
    public String deleteEntry(@PathVariable("id") Long entryId, HttpSession session, Model model) throws EntryNotFoundException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Entry entry = entryService.getEntryById(entryId, currentUser);
        Long listId = entry.getWishlist().getId();
        entryService.deleteEntry(entryId, currentUser);
        return "redirect:/lists/" + listId;
    }

    @GetMapping("/lists/{listId}/entries/filter")
    public String filterEntries(@PathVariable("listId") Long listId,
                                @RequestParam Map<String, String> filters,
                                HttpSession session,
                                Model model) throws EntryNotFoundException {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Entry> filteredEntries = entryService.filterEntries(listId, filters, currentUser);

        model.addAttribute("entries", filteredEntries);
        model.addAttribute("listId", listId);

        return "entry/list_view";
    }
}
