package com.staten.capstone.service;

import com.staten.capstone.UserAlreadyExistsException;
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

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleDao roleDao;

    @Override
    public User registerNewUser(UserDto userDto) throws UserAlreadyExistsException {

        if (usernameExists(userDto.getUsername())) {
            // if username already exists
            String username = userDto.getUsername();
            userDto.setUsername(""); // reset username to empty string
            throw new UserAlreadyExistsException("There is already an account with this username: " + username);
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
    public User findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findUserById(Integer id) {
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

    // getUser

    // saveRegisteredUser

    // deleteUser

    // returns true if username already exists in system
    private boolean usernameExists(final String username) {
        return userDao.findByUsername(username) != null;
    }
}
