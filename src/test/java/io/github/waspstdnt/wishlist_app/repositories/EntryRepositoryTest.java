//package io.github.waspstdnt.wishlist_app.repositories;
//
//import io.github.waspstdnt.wishlist_app.models.Entry;
//import io.github.waspstdnt.wishlist_app.models.User;
//import io.github.waspstdnt.wishlist_app.models.Wishlist;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ActiveProfiles("test")
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class EntryRepositoryTest {
//
//    @Autowired
//    private EntryRepository entryRepository;
//
//    @Autowired
//    private WishlistRepository wishlistRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void testSaveAndFindEntryByWishlist() {
//        User user = User.builder()
//                .username("entryUser")
//                .email("entryuser@example.com")
//                .password("hashed_password")
//                .build();
//        userRepository.save(user);
//
//        Wishlist wishlist = Wishlist.builder()
//                .title("Book List")
//                .user(user)
//                .build();
//        wishlistRepository.save(wishlist);
//
//        Map<String, Object> entryData = Map.of(
//                "title", "1984",
//                "author", "George Orwell",
//                "pages", 328,
//                "read", false
//        );
//        Entry entry = Entry.builder()
//                .title("1984")
//                .wishlist(wishlist)
//                .data(entryData)
//                .build();
//        entryRepository.save(entry);
//
//        List<Entry> entries = entryRepository.findByWishlist(wishlist);
//        assertThat(entries).isNotEmpty();
//        assertThat(entries.get(0).getData().get("author").toString()).isEqualTo("George Orwell");
//
//        Optional<Entry> found = entryRepository.findById(entry.getId());
//        assertThat(found).isPresent();
//        assertThat(found.get().getTitle()).isEqualTo("1984");
//    }
//}
