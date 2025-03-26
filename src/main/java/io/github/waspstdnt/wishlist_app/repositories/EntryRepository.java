package io.github.waspstdnt.wishlist_app.repositories;

import io.github.waspstdnt.wishlist_app.models.Entry;
import io.github.waspstdnt.wishlist_app.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByWishlist(Wishlist wishlist);
}
