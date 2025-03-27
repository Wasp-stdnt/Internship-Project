package io.github.waspstdnt.wishlist_app.services;

import io.github.waspstdnt.wishlist_app.dtos.UserRegistrationDto;
import io.github.waspstdnt.wishlist_app.exceptions.InvalidCredentialsException;
import io.github.waspstdnt.wishlist_app.exceptions.UserAlreadyExistsException;
import io.github.waspstdnt.wishlist_app.exceptions.UserNotFoundException;
import io.github.waspstdnt.wishlist_app.models.User;
import io.github.waspstdnt.wishlist_app.repositories.UserRepository;
import io.github.waspstdnt.wishlist_app.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegisterUser_Success() throws UserAlreadyExistsException {
        UserRegistrationDto dto = UserRegistrationDto.builder()
                .username("testuser")
                .email("test@example.com")
                .password("rawpassword")
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawpassword")).thenReturn("hashedpassword");
        User savedUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("hashedpassword")
                .build();
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getPassword()).isEqualTo("hashedpassword");
    }

    @Test
    public void testAuthenticateUser_InvalidPassword() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("hashedpassword")
                .build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashedpassword")).thenReturn(false);

        assertThatThrownBy(() ->
                userService.authenticateUser("test@example.com", "wrongpassword")
        ).isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    public void testUpdateUser_Success() throws UserNotFoundException {
        User existingUser = User.builder()
                .id(1L)
                .username("oldUser")
                .email("old@example.com")
                .password("oldHash")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("newHash");
        User updatedUser = User.builder()
                .id(1L)
                .username("newUser")
                .email("new@example.com")
                .password("newHash")
                .build();
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(updatedUser);

        UserRegistrationDto dto = UserRegistrationDto.builder()
                .username("newUser")
                .email("new@example.com")
                .password("newpassword")
                .build();

        User result = userService.updateUser(1L, dto);
        assertThat(result.getUsername()).isEqualTo("newUser");
        assertThat(result.getEmail()).isEqualTo("new@example.com");
        assertThat(result.getPassword()).isEqualTo("newHash");
    }

    @Test
    public void testDeleteUser_Success() throws UserNotFoundException {
        User existingUser = User.builder()
                .id(1L)
                .username("deleteUser")
                .email("delete@example.com")
                .password("hash")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).delete(existingUser);

        userService.deleteUser(1L);
        verify(userRepository, times(1)).delete(existingUser);
    }
}