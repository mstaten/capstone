package com.staten.capstone.models.data;

import com.staten.capstone.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Integer> {

    public User findByUsername(String username);

    public User findById(int id);

    @Override
    void delete(User user);
}
