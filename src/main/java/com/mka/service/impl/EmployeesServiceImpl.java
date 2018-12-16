package com.mka.service.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.AccountsDao;
import com.mka.dao.EmployeesDao;
import com.mka.model.AccountPayableReceivable;
import com.mka.model.Employees;
import com.mka.model.EmployeessPayments;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import com.mka.service.AccountsService;
import com.mka.service.EmployeesService;
import com.mka.service.EntriesService;
import com.mka.service.UserService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import java.math.BigDecimal;
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

    @Autowired
    AsyncUtil asyncUtil;

    @Autowired
    AccountsService accountsService;

    @Autowired
    UserService userService;

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
        if (employeesDao.addEmployee(emp)) {
            userService.addCustomerAndBuyer(emp.getName());
            return true;
        }
        return false;
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
        if (employees != null && !employees.isEmpty()) {
            for (Employees emp : employees) {
                if (!emp.getCurrentMonthPayed() && !emp.getIsTerminated()) {
                    totalAmount += emp.getSalary().intValue();
                    EmployeessPayments empPayment = new EmployeessPayments();
                    empPayment.setAmountPayed(emp.getSalary());
                    empPayment.setEmployees(emp);
                    empPayment.setPaymentDate(new Date());
                    paymentRecord.put(emp, empPayment);

                    AccountPayableReceivable payable = new AccountPayableReceivable();
                    payable.setAccountName(emp.getName());
                    payable.setAmount(emp.getSalary());
                    payable.setQuantity(BigDecimal.ZERO);
                    payable.setRate(BigDecimal.ZERO);
                    payable.setTotalAmount(emp.getSalary());
                    payable.setEntryId(0);
                    payable.setIsActive(true);
                    payable.setProject(" ");
                    payable.setType(Constants.PAYABLE);
                    payable.setSubType(Constants.EXPENSE);
                    payable.setItemType(new EntryItems(22));

                    if (!accountsService.logAccountPayableReceivable(payable)) {
                        log.warn("*** Account Payable not logged ***");
                    } else {
                        // update users account
                        asyncUtil.updateAccount(payable);
                    }

                } else {
                    log.warn("Employees Current Month Salary is already paid: " + emp.toString());
                }
            }
            if (!paymentRecord.isEmpty()) {
                boolean salariesPayed = employeesDao.payAllEmployees(paymentRecord);
                if (salariesPayed) {
                    // indirect salaries expense
//                    EntriesIndirect entry = new EntriesIndirect();
//                    entry.setItem(entriesService.createNewEntryItem("Salaries"));
//                    entry.setName("Employees Salaries");
//                    entry.setDescription("Employees Salaries Payed");
//                    entry.setAmount(BigDecimal.valueOf(totalAmount));
//                    entry.setAdvance(BigDecimal.ZERO);
//                    entry.setEntryDate(new Date());
//                    entry.setIsActive(true);
//
//                    boolean entryLogged = entriesService.logInDirectEntry(entry);
//                    if (!entryLogged) {
//                        log.warn("Failed to Log Salaries Indirect Entry");
//                    }
                    return salariesPayed;
                }
            }
        }

        return false;
    }

}
