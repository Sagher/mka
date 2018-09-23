/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.service;

import com.mka.model.Employees;
import java.util.List;

/**
 *
 * @author Sagher Mehmood
 */
public interface EmployeesService {

    public List<Employees> getEmployees(int startIndex, int fetchSize, String orderBy, String sortBy, String searchBy);

    public int getEmployeesCount(String searchBy);

    public boolean addEmployee(Employees emp);

    public boolean updateEmployee(Employees emp);

    public Employees getEmployee(int id);
}
