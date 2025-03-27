package io.github.waspstdnt.wishlist_app.services;

import io.github.waspstdnt.wishlist_app.dtos.WishlistDto;
import io.github.waspstdnt.wishlist_app.exceptions.WishlistNotFoundException;
import io.github.waspstdnt.wishlist_app.models.Template;
import io.github.waspstdnt.wishlist_app.models.User;
import io.github.waspstdnt.wishlist_app.models.Wishlist;
import io.github.waspstdnt.wishlist_app.repositories.TemplateRepository;
import io.github.waspstdnt.wishlist_app.repositories.WishlistRepository;
import io.github.waspstdnt.wishlist_app.services.impl.WishlistServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishlistServiceImplTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private TemplateRepository templateRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Test
    public void testCreateWishlist_WithTemplate_Success() {
        User user = User.builder().id(1L).username("user1").build();
        WishlistDto dto = WishlistDto.builder().title("My List").templateId(10L).build();
        Template template = Template.builder().id(10L).title("Books").build();

        when(templateRepository.findById(10L)).thenReturn(Optional.of(template));
        Wishlist wishlistToSave = Wishlist.builder().title("My List").user(user).template(template).build();

        Wishlist savedWishlist = Wishlist.builder().id(100L).title("My List").user(user).template(template).build();
        when(wishlistRepository.save(ArgumentMatchers.any(Wishlist.class))).thenReturn(savedWishlist);

        Wishlist result = wishlistService.createWishlist(dto, user);

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getTitle()).isEqualTo("My List");
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getTemplate()).isEqualTo(template);
    }

    @Test
    public void testGetWishlistById_NotOwned_ThrowsException() {
        User owner = User.builder().id(1L).build();
        User anotherUser = User.builder().id(2L).build();
        Wishlist wishlist = Wishlist.builder().id(50L).user(owner).build();
        when(wishlistRepository.findById(50L)).thenReturn(Optional.of(wishlist));

        assertThatThrownBy(() -> wishlistService.getWishlistById(50L, anotherUser))
                .isInstanceOf(WishlistNotFoundException.class);
    }

    @Test
    public void testGetWishlistsByUser() {
        User user = User.builder().id(1L).username("wishlistUser").build();
        Wishlist wishlist1 = Wishlist.builder().id(10L).title("List1").user(user).build();
        Wishlist wishlist2 = Wishlist.builder().id(11L).title("List2").user(user).build();
        when(wishlistRepository.findByUser(user)).thenReturn(List.of(wishlist1, wishlist2));

        List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
        assertThat(wishlists).hasSize(2);
        assertThat(wishlists).extracting("title").contains("List1", "List2");
    }

    @Test
    public void testUpdateWishlist_Success() throws WishlistNotFoundException {
        User user = User.builder().id(1L).build();
        Wishlist existingWishlist = Wishlist.builder().id(10L).title("Old Title").user(user).build();
        when(wishlistRepository.findById(10L)).thenReturn(Optional.of(existingWishlist));
        Wishlist updatedWishlist = Wishlist.builder().id(10L).title("New Title").user(user).build();
        when(wishlistRepository.save(ArgumentMatchers.any(Wishlist.class))).thenReturn(updatedWishlist);
        WishlistDto dto = WishlistDto.builder().title("New Title").build();

        Wishlist result = wishlistService.updateWishlist(10L, dto, user);
        assertThat(result.getTitle()).isEqualTo("New Title");
    }

    @Test
    public void testDeleteWishlist_Success() throws WishlistNotFoundException {
        User user = User.builder().id(1L).build();
        Wishlist existingWishlist = Wishlist.builder().id(10L).title("To Delete").user(user).build();
        when(wishlistRepository.findById(10L)).thenReturn(Optional.of(existingWishlist));
        doNothing().when(wishlistRepository).delete(existingWishlist);

        wishlistService.deleteWishlist(10L, user);
        verify(wishlistRepository, times(1)).delete(existingWishlist);
    }
}
