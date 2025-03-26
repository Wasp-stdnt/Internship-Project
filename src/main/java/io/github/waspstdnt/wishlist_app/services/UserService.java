package io.github.waspstdnt.wishlist_app.services;

import io.github.waspstdnt.wishlist_app.dtos.UserRegistrationDto;
import io.github.waspstdnt.wishlist_app.exceptions.InvalidCredentialsException;
import io.github.waspstdnt.wishlist_app.exceptions.UserAlreadyExistsException;
import io.github.waspstdnt.wishlist_app.exceptions.UserNotFoundException;
import io.github.waspstdnt.wishlist_app.models.User;

public interface UserService {

    /**
     * Registers a new user.
     * @param registrationDto Data Transfer Object containing registration details.
     * @return The newly registered User.
     * @throws UserAlreadyExistsException if the email or username is already taken.
     */
    User registerUser(UserRegistrationDto registrationDto) throws UserAlreadyExistsException;

    /**
     * Authenticates a user.
     * @param usernameOrEmail The user's username or email.
     * @param password The raw password to verify.
     * @return The authenticated User.
     * @throws InvalidCredentialsException if the credentials are invalid.
     * @throws UserNotFoundException if no matching user is found.
     */
    User authenticateUser(String usernameOrEmail, String password)
            throws InvalidCredentialsException, UserNotFoundException;

    /**
     * Updates the details of an existing user.
     * @param userId The id of the user to update.
     * @param registrationDto Data Transfer Object with updated details.
     * @return The updated User.
     * @throws UserNotFoundException if no user with the provided id exists.
     */
    User updateUser(Long userId, UserRegistrationDto registrationDto) throws UserNotFoundException;

    /**
     * Deletes an existing user.
     * @param userId The id of the user to delete.
     * @throws UserNotFoundException if no user with the provided id exists.
     */
    void deleteUser(Long userId) throws UserNotFoundException;
}
