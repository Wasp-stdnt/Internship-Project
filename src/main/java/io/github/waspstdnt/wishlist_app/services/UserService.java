package io.github.waspstdnt.wishlist_app.services;

import io.github.waspstdnt.wishlist_app.dtos.UserRegistrationDto;
import io.github.waspstdnt.wishlist_app.exceptions.InvalidCredentialsException;
import io.github.waspstdnt.wishlist_app.exceptions.UserAlreadyExistsException;
import io.github.waspstdnt.wishlist_app.exceptions.UserNotFoundException;
import io.github.waspstdnt.wishlist_app.models.User;

public interface UserService {

    User registerUser(UserRegistrationDto registrationDto) throws UserAlreadyExistsException;

    User authenticateUser(String usernameOrEmail, String password)
            throws InvalidCredentialsException, UserNotFoundException;

    User updateUser(Long userId, UserRegistrationDto registrationDto) throws UserNotFoundException;

    void deleteUser(Long userId) throws UserNotFoundException;
}
