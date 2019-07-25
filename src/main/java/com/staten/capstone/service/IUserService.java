package com.staten.capstone.service;

import com.staten.capstone.UserAlreadyExistsException;
import com.staten.capstone.models.User;
import com.staten.capstone.models.data.UserDto;

public interface IUserService {

    User registerNewUser(final UserDto userDto) throws UserAlreadyExistsException;

    User findUserByUsername(final String username);

    User findUserById(final Integer id);

    Boolean checkPasswords(final User user, final String oldPassword);

    void changeUserPassword(final User user, final String password);

    // getUser

    // saveRegisteredUser

    // deleteUser
}
