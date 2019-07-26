package com.staten.capstone.service;

import com.staten.capstone.errors.PasswordsMismatchException;
import com.staten.capstone.errors.UserAlreadyExistsException;
import com.staten.capstone.models.Role;
import com.staten.capstone.models.User;
import com.staten.capstone.models.data.RoleDao;
import com.staten.capstone.models.data.UserDao;
import com.staten.capstone.models.data.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to register users and add to database
 */
@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleDao roleDao;

    /**
     * Register new user
     *
     * @param userDto
     * @return user
     * @throws UserAlreadyExistsException
     */
    @Override
    public User registerNewUser(UserDto userDto) throws UserAlreadyExistsException, PasswordsMismatchException {

        if (usernameExists(userDto.getUsername())) {
            // if username already exists
            String username = userDto.getUsername();
            userDto.setUsername(""); // reset username to empty string
            throw new UserAlreadyExistsException("There is already an account with this username: " + username);
        }

        // check if password and verify string match
        if (!verifyNewUserPasswords(userDto.getPassword(), userDto.getVerify())) {
            // if don't match, return error
            throw new PasswordsMismatchException("Passwords don't match");
        }

        // create new user w/given fields
        final User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        List<Role> roles = new ArrayList<>();
        roles.add(roleDao.findByName("ROLE_USER")); // automatically make role USER
        user.setRoles(roles);

        return userDao.save(user);
    }

    @Override
    public User findUserByUsername(final String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findUserById(final Integer id) {
        return userDao.findOne(id);
    }

    @Override
    public Boolean checkPasswords(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userDao.save(user);
    }

    @Override
    public Boolean verifyNewUserPasswords(final String password, final String verify) {
        return (password.equals(verify));
    }

    // getUser

    // saveRegisteredUser

    // deleteUser

    // returns true if username already exists in system
    private boolean usernameExists(final String username) {
        return userDao.findByUsername(username) != null;
    }
}
