package io.github.waspstdnt.wishlist_app.repositories;

import io.github.waspstdnt.wishlist_app.models.User;
import io.github.waspstdnt.wishlist_app.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(User user);
}
