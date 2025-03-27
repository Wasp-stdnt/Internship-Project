package io.github.waspstdnt.wishlist_app.services.impl;

import io.github.waspstdnt.wishlist_app.dtos.UserRegistrationDto;
import io.github.waspstdnt.wishlist_app.exceptions.InvalidCredentialsException;
import io.github.waspstdnt.wishlist_app.exceptions.UserAlreadyExistsException;
import io.github.waspstdnt.wishlist_app.exceptions.UserNotFoundException;
import io.github.waspstdnt.wishlist_app.models.User;
import io.github.waspstdnt.wishlist_app.repositories.UserRepository;
import io.github.waspstdnt.wishlist_app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(UserRegistrationDto registrationDto) throws UserAlreadyExistsException {
        Optional<User> existingByEmail = userRepository.findByEmail(registrationDto.getEmail());
        if (existingByEmail.isPresent()) {
            throw new UserAlreadyExistsException("Користувач з емейлом " + registrationDto.getEmail() + " вже існує.");
        }

        Optional<User> existingByUsername = userRepository.findByUsername(registrationDto.getUsername());
        if (existingByUsername.isPresent()) {
            throw new UserAlreadyExistsException("Користувач з ім'ям " + registrationDto.getUsername() + " вже існує.");
        }

        String hashedPassword = passwordEncoder.encode(registrationDto.getPassword());

        User user = User.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(hashedPassword)
                .build();

        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(String usernameOrEmail, String password)
            throws InvalidCredentialsException, UserNotFoundException {
        Optional<User> userOpt = userRepository.findByEmail(usernameOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByUsername(usernameOrEmail);
            if (userOpt.isEmpty()) {
                throw new UserNotFoundException("Користувача з наступним емейлом/ім'ям не знайдено: " + usernameOrEmail);
            }
        }
        User user = userOpt.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Надано невірний пароль.");
        }
        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long userId, UserRegistrationDto registrationDto) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());

        if (registrationDto.getPassword() != null && !registrationDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
    }
}
