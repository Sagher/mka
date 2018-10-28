package com.mka.dao;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.model.Employees;
import com.mka.model.EmployeessPayments;
import java.util.List;
import java.util.Map;

public interface EmployeesDao {

    public List<Employees> getEmployees(int startIndex, int fetchSize, String orderBy, String sortBy, String searchBy);

    public int getEmployeesCount(String searchBy);

    public boolean addEmployee(Employees emp);

    public boolean updateEmployee(Employees emp);

    public Employees getEmployee(int id);

    public boolean payAllEmployees(Map<Employees, EmployeessPayments> paymentRecord);
}
