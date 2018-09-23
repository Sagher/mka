package com.mka.service.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EmployeesDao;
import com.mka.model.Employees;
import com.mka.service.EmployeesService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("employeesService")
public class EmployeesServiceImpl implements EmployeesService {

    private static final Logger log = Logger.getLogger(EmployeesServiceImpl.class);

    @Autowired
    EmployeesDao employeesDao;

    @Override
    public List<Employees> getEmployees(int startIndex, int fetchSize, String orderBy, String sortBy, String searchBy) {
        return employeesDao.getEmployees(startIndex, fetchSize, orderBy, sortBy, searchBy);
    }

    @Override
    public int getEmployeesCount(String searchBy) {
        return employeesDao.getEmployeesCount(searchBy);
    }

    @Override
    public boolean addEmployee(Employees emp) {
        return employeesDao.addEmployee(emp);
    }

    @Override
    public boolean updateEmployee(Employees emp) {
        return employeesDao.updateEmployee(emp);
    }

    @Override
    public Employees getEmployee(int id) {
        return employeesDao.getEmployee(id);
    }

}
