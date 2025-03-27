package io.github.waspstdnt.wishlist_app.services;

import io.github.waspstdnt.wishlist_app.dtos.WishlistDto;
import io.github.waspstdnt.wishlist_app.exceptions.WishlistNotFoundException;
import io.github.waspstdnt.wishlist_app.models.User;
import io.github.waspstdnt.wishlist_app.models.Wishlist;

import java.util.List;

public interface WishlistService {

    Wishlist createWishlist(WishlistDto wishlistDto, User currentUser);

    List<Wishlist> getWishlistsByUser(User currentUser);

    Wishlist getWishlistById(Long id, User currentUser) throws WishlistNotFoundException;

    Wishlist updateWishlist(Long id, WishlistDto wishlistDto, User currentUser) throws WishlistNotFoundException;

    void deleteWishlist(Long id, User currentUser) throws WishlistNotFoundException;
}
