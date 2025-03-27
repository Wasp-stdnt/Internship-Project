package io.github.waspstdnt.wishlist_app.services;

import io.github.waspstdnt.wishlist_app.dtos.EntryDto;
import io.github.waspstdnt.wishlist_app.exceptions.EntryNotFoundException;
import io.github.waspstdnt.wishlist_app.exceptions.InvalidEntryDataException;
import io.github.waspstdnt.wishlist_app.models.Entry;
import io.github.waspstdnt.wishlist_app.models.User;
import io.github.waspstdnt.wishlist_app.models.Wishlist;
import io.github.waspstdnt.wishlist_app.repositories.EntryRepository;
import io.github.waspstdnt.wishlist_app.repositories.WishlistRepository;
import io.github.waspstdnt.wishlist_app.services.impl.EntryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntryServiceImplTest {

    @Mock
    private EntryRepository entryRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private EntryServiceImpl entryService;

    @Test
    public void testCreateEntry_Success() throws InvalidEntryDataException {
        User user = User.builder().id(1L).username("user1").build();
        Wishlist wishlist = Wishlist.builder().id(10L).user(user).build();
        EntryDto dto = EntryDto.builder()
                .title("1984")
                .data(Map.of("author", "George Orwell", "pages", 328, "read", false))
                .build();

        when(wishlistRepository.findById(10L)).thenReturn(Optional.of(wishlist));

        Entry entryToSave = Entry.builder().title("1984").wishlist(wishlist).data(dto.getData()).build();
        Entry savedEntry = Entry.builder().id(100L).title("1984").wishlist(wishlist).data(dto.getData()).build();
        when(entryRepository.save(ArgumentMatchers.any(Entry.class))).thenReturn(savedEntry);

        Entry result = entryService.createEntry(10L, dto, user);

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getTitle()).isEqualTo("1984");
        assertThat(result.getData()).containsEntry("author", "George Orwell");
    }

    @Test
    public void testFilterEntries_MultipleConstraints() {
        User user = User.builder().id(1L).build();
        Wishlist wishlist = Wishlist.builder().id(20L).user(user).build();
        Entry entry1 = Entry.builder()
                .id(1L)
                .title("Book A")
                .wishlist(wishlist)
                .data(Map.of("author", "AuthorX", "pages", 350, "read", false))
                .build();
        Entry entry2 = Entry.builder()
                .id(2L)
                .title("Book B")
                .wishlist(wishlist)
                .data(Map.of("author", "AuthorX", "pages", 280, "read", false))
                .build();
        Entry entry3 = Entry.builder()
                .id(3L)
                .title("Book C")
                .wishlist(wishlist)
                .data(Map.of("author", "AuthorY", "pages", 400, "read", false))
                .build();
        when(wishlistRepository.findById(20L)).thenReturn(Optional.of(wishlist));
        when(entryRepository.findByWishlist(wishlist)).thenReturn(List.of(entry1, entry2, entry3));

        Map<String, String> filters = Map.of(
                "author", "AuthorX",
                "pages_min", "300",
                "pages_max", "500",
                "read", "false"
        );

        List<Entry> filtered = entryService.filterEntries(20L, filters, user);

        assertThat(filtered).hasSize(1);
        assertThat(filtered.get(0).getId()).isEqualTo(1L);
    }

    @Test
    public void testGetEntryById_NotOwned_ThrowsException() {
        User owner = User.builder().id(1L).build();
        User anotherUser = User.builder().id(2L).build();
        Wishlist wishlist = Wishlist.builder().id(10L).user(owner).build();
        Entry entry = Entry.builder().id(100L).title("Test Entry").wishlist(wishlist).data(Map.of("key", "value")).build();
        when(entryRepository.findById(100L)).thenReturn(Optional.of(entry));

        assertThatThrownBy(() -> entryService.getEntryById(100L, anotherUser))
                .isInstanceOf(EntryNotFoundException.class);
    }

    @Test
    public void testUpdateEntry_Success() throws EntryNotFoundException, InvalidEntryDataException {
        User user = User.builder().id(1L).build();
        Wishlist wishlist = Wishlist.builder().id(20L).user(user).build();
        Entry existingEntry = Entry.builder()
                .id(100L)
                .title("Old Entry")
                .wishlist(wishlist)
                .data(Map.of("key", "oldValue"))
                .build();
        when(entryRepository.findById(100L)).thenReturn(Optional.of(existingEntry));

        Map<String, Object> newData = Map.of("key", "newValue");
        EntryDto dto = EntryDto.builder().title("New Entry").data(newData).build();

        Entry updatedEntry = Entry.builder()
                .id(100L)
                .title("New Entry")
                .wishlist(wishlist)
                .data(newData)
                .build();
        when(entryRepository.save(ArgumentMatchers.any(Entry.class))).thenReturn(updatedEntry);

        Entry result = entryService.updateEntry(100L, dto, user);
        assertThat(result.getTitle()).isEqualTo("New Entry");
        assertThat(result.getData()).containsEntry("key", "newValue");
    }

    @Test
    public void testDeleteEntry_Success() throws EntryNotFoundException {
        User user = User.builder().id(1L).build();
        Wishlist wishlist = Wishlist.builder().id(20L).user(user).build();
        Entry existingEntry = Entry.builder()
                .id(100L)
                .title("To Delete")
                .wishlist(wishlist)
                .data(Map.of("key", "value"))
                .build();
        when(entryRepository.findById(100L)).thenReturn(Optional.of(existingEntry));

        entryService.deleteEntry(100L, user);
        verify(entryRepository, times(1)).delete(existingEntry);
    }
}
