package io.github.waspstdnt.wishlist_app.services.impl;

import io.github.waspstdnt.wishlist_app.dtos.EntryDto;
import io.github.waspstdnt.wishlist_app.exceptions.EntryNotFoundException;
import io.github.waspstdnt.wishlist_app.exceptions.InvalidEntryDataException;
import io.github.waspstdnt.wishlist_app.models.Entry;
import io.github.waspstdnt.wishlist_app.models.User;
import io.github.waspstdnt.wishlist_app.models.Wishlist;
import io.github.waspstdnt.wishlist_app.repositories.EntryRepository;
import io.github.waspstdnt.wishlist_app.repositories.WishlistRepository;
import io.github.waspstdnt.wishlist_app.services.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;
    private final WishlistRepository wishlistRepository;

    @Override
    @Transactional
    public Entry createEntry(Long wishlistId, EntryDto entryDto, User currentUser) throws InvalidEntryDataException {
        Wishlist wishlist = getVerifiedWishlist(wishlistId, currentUser);

        Entry entry = Entry.builder()
                .title(entryDto.getTitle())
                .data(entryDto.getData())
                .wishlist(wishlist)
                .build();

        return entryRepository.save(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entry> getEntriesByWishlist(Long wishlistId, User currentUser) {
        Wishlist wishlist = getVerifiedWishlist(wishlistId, currentUser);
        return entryRepository.findByWishlist(wishlist);
    }

    @Override
    @Transactional(readOnly = true)
    public Entry getEntryById(Long entryId, User currentUser) throws EntryNotFoundException {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntryNotFoundException("Entry not found with id: " + entryId));
        if (!entry.getWishlist().getUser().getId().equals(currentUser.getId())) {
            throw new EntryNotFoundException("Entry not found for current user with id: " + entryId);
        }
        return entry;
    }

    @Override
    @Transactional
    public Entry updateEntry(Long entryId, EntryDto entryDto, User currentUser) throws EntryNotFoundException, InvalidEntryDataException {
        Entry entry = getEntryById(entryId, currentUser);
        entry.setTitle(entryDto.getTitle());
        entry.setData(entryDto.getData());
        return entryRepository.save(entry);
    }

    @Override
    @Transactional
    public void deleteEntry(Long entryId, User currentUser) throws EntryNotFoundException {
        Entry entry = getEntryById(entryId, currentUser);
        entryRepository.delete(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entry> filterEntries(Long wishlistId, Map<String, String> filters, User currentUser) {
        Wishlist wishlist = getVerifiedWishlist(wishlistId, currentUser);
        List<Entry> entries = entryRepository.findByWishlist(wishlist);

        return entries.stream().filter(entry -> {
            Map<String, Object> data = entry.getData();

            for (Map.Entry<String, String> filter : filters.entrySet()) {
                String key = filter.getKey();
                String value = filter.getValue();

                if (key.endsWith("_min") || key.endsWith("_max")) {
                    String field = key.replace("_min", "").replace("_max", "");
                    Object fieldValue = data.get(field);
                    if (fieldValue == null) return false;

                    try {
                        double numValue = Double.parseDouble(fieldValue.toString());
                        double filterValue = Double.parseDouble(value);

                        if (key.endsWith("_min") && numValue < filterValue) return false;
                        if (key.endsWith("_max") && numValue > filterValue) return false;

                    } catch (NumberFormatException e) {
                        return false;
                    }
                } else {
                    Object fieldValue = data.get(key);
                    if (fieldValue == null || !fieldValue.toString().equalsIgnoreCase(value)) {
                        return false;
                    }
                }
            }
            return true;
        }).collect(Collectors.toList());
    }

    private Wishlist getVerifiedWishlist(Long wishlistId, User currentUser) {
        Optional<Wishlist> wishlistOpt = wishlistRepository.findById(wishlistId);
        if (wishlistOpt.isEmpty() || !wishlistOpt.get().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Wishlist not found or not owned by current user with id: " + wishlistId);
        }
        return wishlistOpt.get();
    }
}

