package com.mka.service.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.UserDao;
import com.mka.model.User;
import com.mka.service.UserService;
import com.mka.utils.AsyncUtil;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger log = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    AsyncUtil asyncUtil;

    @Autowired
    UserDao userDao;

    private List<User> usersList = null;

    @Override
    public List<User> getAllUsers() {
        if (usersList == null || usersList.isEmpty()) {
            usersList = userDao.getAllUsers();
        }
        return usersList;
    }

    public Integer getUsersCount() {
        if (usersList == null || usersList.isEmpty()) {
            usersList = userDao.getAllUsers();
        }
        return usersList.size();
    }

    @Override
    public User loginUser(String userName, String password) {
        return userDao.loginUser(userName, password);
    }

    @Override
    public User getUser(int id) {
        if (usersList != null) {
            try {
                return usersList.parallelStream().filter(e -> e.getId() == id).collect(Collectors.toList()).get(0);
            } catch (Exception e) {
                log.error("Error in getUser(" + id + "): " + e.getMessage());
            }
        }
        return userDao.getUser(id);
    }

    @Override
    public User getUser(String username) {
        if (usersList != null) {
            try {
                return usersList.parallelStream().filter(e -> e.getUsername().equals(username)).collect(Collectors.toList()).get(0);
            } catch (Exception e) {
                log.error("Error in getUser(" + username + "): " + e.getMessage());
            }
        }
        return userDao.getUser(username);
    }

    @Override
    public boolean addUser(User user) {
        boolean response = userDao.addUser(user);
        if (response) {
            usersList = userDao.getAllUsers();
        }
        return response;
    }

    @Override
    public boolean updateUser(User user) {
        boolean response = userDao.updateUser(user);
        if (response) {
            usersList = userDao.getAllUsers();
        }
        return response;
    }

}
