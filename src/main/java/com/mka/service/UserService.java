package com.mka.service;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.model.CustomersBuyers;
import com.mka.model.Projects;
import com.mka.model.User;
import java.util.List;

public interface UserService {

    public List<User> getAllUsers();

    public Integer getUsersCount();

    public User loginUser(String userName, String password);

    public User getUser(int id);

    public User getUser(String username);

    public boolean addUser(User user);

    public boolean updateUser(User user);

    public List<CustomersBuyers> getCustomersAndBuyers();

    public CustomersBuyers getCustomerAndBuyer(String name);

    public boolean addCustomerAndBuyer(String cusBuy);

    public boolean updateCustomerAndBuyer(CustomersBuyers cusBuy);

    public List<Projects> getProjects();

    public boolean addProject(String proj);
}
