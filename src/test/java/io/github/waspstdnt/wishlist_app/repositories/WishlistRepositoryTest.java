//package io.github.waspstdnt.wishlist_app.repositories;
//
//import io.github.waspstdnt.wishlist_app.models.User;
//import io.github.waspstdnt.wishlist_app.models.Wishlist;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ActiveProfiles("test")
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class WishlistRepositoryTest {
//
//    @Autowired
//    private WishlistRepository wishlistRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void testSaveAndFindWishlistByUser() {
//        User user = User.builder()
//                .username("wishlistUser")
//                .email("wishlistuser@example.com")
//                .password("hashed_password")
//                .build();
//        userRepository.save(user);
//
//        Wishlist wishlist = Wishlist.builder()
//                .title("My Movies")
//                .user(user)
//                .build();
//        wishlistRepository.save(wishlist);
//
//        List<Wishlist> wishlists = wishlistRepository.findByUser(user);
//        assertThat(wishlists).isNotEmpty();
//        assertThat(wishlists.get(0).getTitle()).isEqualTo("My Movies");
//    }
//}
