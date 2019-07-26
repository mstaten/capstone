package com.staten.capstone.service;

import com.staten.capstone.errors.PasswordsMismatchException;
import com.staten.capstone.errors.UserAlreadyExistsException;
import com.staten.capstone.models.User;
import com.staten.capstone.models.data.UserDto;

public interface IUserService {

    User registerNewUser(final UserDto userDto) throws UserAlreadyExistsException, PasswordsMismatchException;

    User findUserByUsername(final String username);

    User findUserById(final Integer id);

    Boolean checkPasswords(final User user, final String oldPassword);

    void changeUserPassword(final User user, final String password);

    Boolean verifyNewUserPasswords(final String password, final String verify);

    // getUser

    // saveRegisteredUser

    // deleteUser
}
