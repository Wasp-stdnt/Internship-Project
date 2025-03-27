package io.github.waspstdnt.wishlist_app.services;

import io.github.waspstdnt.wishlist_app.dtos.EntryDto;
import io.github.waspstdnt.wishlist_app.exceptions.EntryNotFoundException;
import io.github.waspstdnt.wishlist_app.exceptions.InvalidEntryDataException;
import io.github.waspstdnt.wishlist_app.models.Entry;
import io.github.waspstdnt.wishlist_app.models.User;

import java.util.List;
import java.util.Map;

public interface EntryService {

    Entry createEntry(Long wishlistId, EntryDto entryDto, User currentUser) throws InvalidEntryDataException;

    List<Entry> getEntriesByWishlist(Long wishlistId, User currentUser);

    Entry getEntryById(Long entryId, User currentUser) throws EntryNotFoundException;

    Entry updateEntry(Long entryId, EntryDto entryDto, User currentUser) throws EntryNotFoundException, InvalidEntryDataException;

    void deleteEntry(Long entryId, User currentUser) throws EntryNotFoundException;

    List<Entry> filterEntries(Long wishlistId, Map<String, String> filters, User currentUser);
}
