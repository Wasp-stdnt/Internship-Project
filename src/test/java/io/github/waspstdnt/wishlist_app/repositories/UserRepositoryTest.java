package io.github.waspstdnt.wishlist_app.repositories;

import io.github.waspstdnt.wishlist_app.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindUserByEmail() {
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("hashed_password")
                .build();

        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testFindUserByUsername() {
        User user = User.builder()
                .username("uniqueUser")
                .email("unique@example.com")
                .password("hashed_password")
                .build();
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("uniqueUser");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("unique@example.com");
    }
}