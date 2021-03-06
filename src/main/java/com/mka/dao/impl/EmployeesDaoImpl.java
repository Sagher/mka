package com.mka.dao.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EmployeesDao;
import com.mka.model.Employees;
import com.mka.model.EmployeessPayments;
import com.mka.utils.Constants;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeesDaoImpl implements EmployeesDao {

    private static final Logger log = Logger.getLogger(EmployeesDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Employees> getEmployees(int startIndex, int fetchSize, String orderBy, String sortBy, String searchBy) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Employees.class);
            criteria.add(Restrictions.eq("isActive", true));
            if (!searchBy.isEmpty()) {
                criteria.add(Restrictions.eq("name", searchBy));
            }
            criteria.setFirstResult(startIndex);
            criteria.setMaxResults(fetchSize);
            if (!sortBy.isEmpty()) {
                if (!orderBy.isEmpty() && orderBy.equalsIgnoreCase("asc")) {
                    criteria.addOrder(Order.asc(sortBy));
                } else {
                    criteria.addOrder(Order.desc(sortBy));
                }
            }

            List<Employees> emps = criteria.list();
            if (emps.size() > 0) {
                return emps;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getEmployees() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public int getEmployeesCount(String searchBy) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Employees.class);
            criteria.add(Restrictions.eq("isActive", true));
            if (!searchBy.isEmpty()) {
                criteria.add(Restrictions.eq("name", searchBy));
            }
            return (((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue());
        } catch (Exception e) {
            log.error("Exception in getEmployeesCount() : ", e);
            return 0;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean addEmployee(Employees emp) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(emp);
            session.save(emp);

            tx.commit();
            session.flush();
            response = true;
            log.info("Employee Added: " + emp);
        } catch (Exception e) {
            log.error("Exception in addEmployee() " + emp.toString(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return response;
    }

    @Override
    public boolean updateEmployee(Employees emp) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            emp.setUpdatedDate(new Date());
            session.evict(emp);
            session.saveOrUpdate(emp);

            tx.commit();
            session.flush();
            response = true;
            log.info("Employee Edited: " + emp);
        } catch (Exception e) {
            log.error("Exception in updateEmployee() " + emp.toString(), e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return response;
    }

    @Override
    public Employees getEmployee(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Employees.class);
            criteria.add(Restrictions.eq("id", id));
            List<Employees> emps = criteria.list();
            if (emps.size() > 0) {
                return emps.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in UserDaoImpl getEmployee() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public Employees getEmployee(String name) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Employees.class);
            criteria.add(Restrictions.eq("name", name));
            List<Employees> emps = criteria.list();
            if (emps.size() > 0) {
                return emps.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in UserDaoImpl getEmployee() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean payAllEmployees(Map<Employees, EmployeessPayments> paymentRecord) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            Iterator it = paymentRecord.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                Map.Entry pair = (Map.Entry) it.next();
                Employees emp = (Employees) pair.getKey();
                EmployeessPayments ep = (EmployeessPayments) pair.getValue();
                it.remove(); // avoids a ConcurrentModificationException
                int res = (Integer) session.save(ep);

                if (res > 0) {
                    emp.setUpdatedDate(new Date());
                    emp.setCurrentMonthPayed(true);
                    session.evict(emp);
                    session.saveOrUpdate(emp);
                }
                if (i % 20 == 0) { // Same as the JDBC batch size
                    //flush a batch of inserts and release memory:
                    session.flush();
                    session.clear();
                }
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Exception in employeesDaoImpl payAllEmployees() : ", e);
            return false;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return true;
    }

    @Override
    public List<EmployeessPayments> getEmployeesPaymentRecord(String from, String to, int empId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(EmployeessPayments.class);

            if (empId > 0) {
                criteria.add(Restrictions.eq("employees", new Employees(empId)));
            }
            if (!from.isEmpty()) {
                from = from + " 00:00:00";
                criteria.add(Restrictions.ge("paymentDate", Constants.TIMESTAMP_FORMAT.parse(from)));
            }
            if (!to.isEmpty()) {
                to = to + " 23:59:59";
                criteria.add(Restrictions.le("paymentDate", Constants.TIMESTAMP_FORMAT.parse(to)));
            }

//            criteria.addOrder(Order.desc("paymentDate"));
            List<EmployeessPayments> emps = criteria.list();
            if (emps.size() > 0) {
                return emps;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getEmployeesPaymentRecord() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public void logEmployeePayment(EmployeessPayments empPayment) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.save(empPayment);

            tx.commit();
        } catch (Exception e) {
            log.error("Exception in employeesDaoImpl logEmployeePayment() : ", e);
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }
}
