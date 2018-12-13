package com.mka.dao.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.UserDao;
import com.mka.model.CustomersBuyers;
import com.mka.model.Projects;
import com.mka.model.User;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    private static final Logger log = Logger.getLogger(UserDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<User> getAllUsers() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(User.class);
            List<User> users = criteria.list();
            if (users.size() > 0) {
                return users;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getAllUsers() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public User loginUser(String userName, String password) {
        User user = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("username", userName));
            criteria.add(Restrictions.eq("password", password));
            if (!criteria.list().isEmpty()) {
                user = (User) criteria.list().get(0);
            }
        } catch (Exception e) {
            log.error("Exception in loginUser() : ", e);
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
        return user;
    }

    @Override
    public User getUser(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("id", id));
            List<User> users = criteria.list();
            if (users.size() > 0) {
                return users.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in UserDaoImpl getUser() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public User getUser(String username) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("username", username));
            List<User> users = criteria.list();
            if (users.size() > 0) {
                return users.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in UserDaoImpl getUser() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean addUser(User user) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(user);
            user.setCreatedDate(new Date());
            user.setUpdatedDate(null);
            session.save(user);

            tx.commit();
            session.flush();
            response = true;
            log.info("User Added: " + user);
        } catch (Exception e) {
            log.error("Exception in addUser() " + user.toString(), e);
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
    public boolean updateUser(User user) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            user.setUpdatedDate(new Date());
            session.evict(user);
            session.saveOrUpdate(user);

            tx.commit();
            session.flush();
            response = true;
            log.info("User Updated: " + user);
        } catch (Exception e) {
            log.error("Exception in updateUser() " + user.toString(), e);
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
    public List<CustomersBuyers> getCustomersAndBuyers() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(CustomersBuyers.class);
            criteria.add(Restrictions.eq("isActive", true));
            List<CustomersBuyers> users = criteria.list();
            if (users.size() > 0) {
                return users;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getCustomersAndBuyers() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public CustomersBuyers getCustomerAndBuyer(String name) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(CustomersBuyers.class);
            criteria.add(Restrictions.eq("isActive", true));
            criteria.add(Restrictions.eq("name", name));
            List<CustomersBuyers> users = criteria.list();
            if (users.size() > 0) {
                return users.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getCustomerAndBuyer() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean addCustomerAndBuyer(CustomersBuyers cusBuy) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(cusBuy);
            cusBuy.setIsActive(true);
            cusBuy.setPayable(BigDecimal.ZERO);
            cusBuy.setReceivable(BigDecimal.ZERO);
            session.save(cusBuy);

            tx.commit();
            session.flush();
            response = true;
            log.info("CustomerAndBuyer Added: " + cusBuy);
        } catch (Exception e) {
            log.error("Exception in addCustomerAndBuyer() " + cusBuy.toString(), e);
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
    public boolean updateCustomerAndBuyer(CustomersBuyers cusBuy) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(cusBuy);
            session.saveOrUpdate(cusBuy);

            tx.commit();
            session.flush();
            response = true;
            log.info("CustomersBuyers Updated: " + cusBuy);
        } catch (Exception e) {
            log.error("Exception in updateCustomerAndBuyer() " + cusBuy.toString(), e);
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
    public List<Projects> getProjects() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(Projects.class);
            criteria.add(Restrictions.eq("isActive", true));
            List<Projects> projects = criteria.list();
            if (projects.size() > 0) {
                return projects;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getProjects() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public boolean addProject(Projects proj) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(proj);
            proj.setIsActive(true);
            session.save(proj);

            tx.commit();
            session.flush();
            response = true;
            log.info("Projects Added: " + proj);
        } catch (Exception e) {
            log.error("Exception in addProject() " + proj.toString(), e);
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

}
