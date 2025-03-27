package io.github.waspstdnt.wishlist_app.services;

import io.github.waspstdnt.wishlist_app.dtos.WishlistDto;
import io.github.waspstdnt.wishlist_app.exceptions.WishlistNotFoundException;
import io.github.waspstdnt.wishlist_app.models.Template;
import io.github.waspstdnt.wishlist_app.models.User;
import io.github.waspstdnt.wishlist_app.models.Wishlist;
import io.github.waspstdnt.wishlist_app.repositories.TemplateRepository;
import io.github.waspstdnt.wishlist_app.repositories.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final TemplateRepository templateRepository;

    @Override
    @Transactional
    public Wishlist createWishlist(WishlistDto wishlistDto, User currentUser) {
        Wishlist.WishlistBuilder wishlistBuilder = Wishlist.builder()
                .title(wishlistDto.getTitle())
                .user(currentUser);

        if (wishlistDto.getTemplateId() != null) {
            Optional<Template> templateOpt = templateRepository.findById(wishlistDto.getTemplateId());
            templateOpt.ifPresent(wishlistBuilder::template);
        }

        Wishlist wishlist = wishlistBuilder.build();
        return wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Wishlist> getWishlistsByUser(User currentUser) {
        return wishlistRepository.findByUser(currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Wishlist getWishlistById(Long id, User currentUser) throws WishlistNotFoundException {
        Optional<Wishlist> wishlistOpt = wishlistRepository.findById(id);
        if (wishlistOpt.isEmpty()) {
            throw new WishlistNotFoundException("Wishlist not found with id: " + id);
        } else if (!wishlistOpt.get().getUser().getId().equals(currentUser.getId())) {
            throw new WishlistNotFoundException("Wishlist not owned by the current user.");
        }
        return wishlistOpt.get();
    }

    @Override
    @Transactional
    public Wishlist updateWishlist(Long id, WishlistDto wishlistDto, User currentUser) throws WishlistNotFoundException {
        Wishlist wishlist = getWishlistById(id, currentUser);

        wishlist.setTitle(wishlistDto.getTitle());

        return wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional
    public void deleteWishlist(Long id, User currentUser) throws WishlistNotFoundException {
        Wishlist wishlist = getWishlistById(id, currentUser);
        wishlistRepository.delete(wishlist);
    }
}
