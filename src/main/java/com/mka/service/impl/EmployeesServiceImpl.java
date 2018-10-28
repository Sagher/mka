package com.mka.service.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EmployeesDao;
import com.mka.model.Employees;
import com.mka.model.EmployeessPayments;
import com.mka.model.EntriesIndirect;
import com.mka.service.EmployeesService;
import com.mka.service.EntriesService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("employeesService")
public class EmployeesServiceImpl implements EmployeesService {

    private static final Logger log = Logger.getLogger(EmployeesServiceImpl.class);

    @Autowired
    EmployeesDao employeesDao;

    @Autowired
    EntriesService entriesService;

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

    @Override
    public boolean payAllEmployees() {
        List<Employees> employees = employeesDao.getEmployees(0, employeesDao.getEmployeesCount(""), "", "", "");
        Map<Employees, EmployeessPayments> paymentRecord = new HashMap<>();
        int totalAmount = 0;
        for (Employees emp : employees) {
            if (!emp.getCurrentMonthPayed() && !emp.getIsTerminated()) {
                totalAmount += emp.getSalary();
                EmployeessPayments empPayment = new EmployeessPayments();
                empPayment.setAmountPayed(emp.getSalary());
                empPayment.setEmployees(emp);
                empPayment.setPaymentDate(new Date());
                paymentRecord.put(emp, empPayment);
            } else {
                log.warn("Employees Current Month Salary is already paid: " + emp.toString());
            }
        }
        if (!paymentRecord.isEmpty()) {
            boolean salariesPayed = employeesDao.payAllEmployees(paymentRecord);
            if (salariesPayed) {
                // indirect salaries expense
                EntriesIndirect entry = new EntriesIndirect();
                entry.setItem(entriesService.createNewEntryItem("Salaries"));
                entry.setName("Employyes Salaries");
                entry.setDescription("Employees Salaries Payed From Portal");
                entry.setAmount(totalAmount);
                entry.setAdvance(0);
                entry.setEntryDate(new Date());
                entry.setIsActive(true);

                boolean entryLogged = entriesService.logInDirectEntry(entry);
                if (!entryLogged) {
                    log.warn("Failed to Log Salaries Indirect Entry");
                }
                return salariesPayed;
            }
        }

        return false;
    }

}
