package com.mka.dao.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.UserActivityDao;
import com.mka.model.User;
import com.mka.model.UserActivity;
import java.util.List;
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
public class UserActivityDaoImpl implements UserActivityDao {

    private static final Logger log = Logger.getLogger(UserActivityDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean addUserActivity(UserActivity userActivity) {
        Session session = null;
        Transaction tx = null;
        boolean response = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.setFlushMode(FlushMode.COMMIT);
            session.evict(userActivity);
            session.save(userActivity);

            tx.commit();
            session.flush();
            response = true;
        } catch (Exception e) {
            log.error("Exception in addUserActivity() " + userActivity.toString(), e);
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
    public List<UserActivity> getUserActivity(User user, int startIndex, int fetchSize, String orderBy) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(UserActivity.class);
            criteria.add(Restrictions.eq("userId", user));
            criteria.setFirstResult(startIndex);
            criteria.setMaxResults(fetchSize);
            if (orderBy.equalsIgnoreCase("asc")) {
                criteria.addOrder(Order.asc("id"));
            } else {
                criteria.addOrder(Order.desc("id"));
            }
            List<UserActivity> users = criteria.list();
            if (users.size() > 0) {
                return users;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception in getUserActivity() : ", e);
            return null;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }

    @Override
    public int getUserActivityCount(User user) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(UserActivity.class);
            criteria.add(Restrictions.eq("userId", user));
            return (((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue());
        } catch (Exception e) {
            log.error("Exception in getUserActivityCount() : ", e);
            return 0;
        } finally {
            if (session != null) {
                session.clear();
                session.close();
            }
        }
    }
}
